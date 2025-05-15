package com.dmolinsky.weatherapp.client;

import com.dmolinsky.weatherapp.model.WeatherData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SmhiWeatherClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SmhiWeatherClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://opendata-download-metfcst.smhi.se")
                .defaultHeader("Accept", "application/json")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public WeatherData getWeather() {
        String urlPath = "/api/category/pmp3g/version/2/geotype/point/lon/18.0202/lat/59.3099/data.json";

        String json = webClient.get()
                .uri(urlPath)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode timeSeries = root.get("timeSeries");

            LocalDateTime targetTime = LocalDateTime.now().plusHours(24).withMinute(0).withSecond(0).withNano(0);

            for (JsonNode entry : timeSeries) {
                String validTime = entry.get("validTime").asText();
                LocalDateTime entryTime = LocalDateTime.parse(validTime, DateTimeFormatter.ISO_DATE_TIME);

                if (entryTime.equals(targetTime)) {
                    double temperature = getParameter(entry, "t");
                    int humidity = (int) getParameter(entry, "r");
                    double windSpeed = getParameter(entry, "ws");
                    int symbol = (int) getParameter(entry, "Wsymb2");

                    String iconFilename = mapSymbolToIconFile(symbol);

                    return new WeatherData("SMHI", temperature, humidity, symbol, windSpeed, entryTime, iconFilename);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read JSON from SMHI.", e);
        }

        return null;
    }

    private double getParameter(JsonNode entry, String name) {
        for (JsonNode param : entry.get("parameters")) {
            if (param.get("name").asText().equals(name)) {
                return param.get("values").get(0).asDouble();
            }
        }
        throw new RuntimeException("Parameter " + name + " not found");
    }

    private String mapSymbolToIconFile(int symbol) {
        return switch (symbol) {
            case 1 -> "day-sunny.png";
            case 2 -> "day-cloudy.png";
            case 3, 4, 5 -> "cloudy.png";
            case 6 -> "fog.png";
            case 7 -> "showers.png";
            case 8, 9 -> "rain.png";
            case 10 -> "sleet.png";
            case 11, 12 -> "snow.png";
            case 13, 15 -> "thunderstorm.png";
            case 14 -> "storm-showers.png";
            default -> "na.png";
        };
    }


}