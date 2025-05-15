package com.dmolinsky.weatherapp.client;

import com.dmolinsky.weatherapp.model.WeatherData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
public class YrWeatherClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public YrWeatherClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.met.no")
                .defaultHeader(HttpHeaders.USER_AGENT, "localhost (diana.molinsky@gmail.com)")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public WeatherData getWeather() {
        String urlPath = "/weatherapi/locationforecast/2.0/compact?lat=59.3099&lon=18.0202";

        String json = webClient.get()
                .uri(urlPath)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode timeSeries = root.get("properties").get("timeseries");

            LocalDateTime targetTime = LocalDateTime.now().plusHours(24).withMinute(0).withSecond(0).withNano(0);

            for (JsonNode entry : timeSeries) {
                String time = entry.get("time").asText();
                LocalDateTime entryTime = ZonedDateTime.parse(time).toLocalDateTime();

                if (entryTime.equals(targetTime)) {
                    JsonNode details = entry.get("data").get("instant").get("details");
                    double temperature = details.get("air_temperature").asDouble();
                    int humidity = (int) details.get("relative_humidity").asDouble();
                    double windSpeed = details.get("wind_speed").asDouble();

                    String symbolCode = entry
                            .get("data")
                            .get("next_1_hours")
                            .get("summary")
                            .get("symbol_code")
                            .asText();

                    String iconFilename = mapSymbolToIconFile(symbolCode);

                    return new WeatherData("Yr", temperature, humidity, 0, windSpeed, entryTime, iconFilename);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read JSON from Yr.", e);
        }

        return null;
    }

    private String mapSymbolToIconFile(String symbol) {
        return switch (symbol) {
            case "clearsky_day" -> "day-sunny.png";
            case "fair_day", "partlycloudy_day" -> "day-cloudy.png";
            case "cloudy", "fog" -> "cloudy.png";
            case "lightrain", "lightrain_showers_day" -> "showers.png";
            case "rain", "rain_showers_day" -> "rain.png";
            case "heavyrain" -> "storm-showers.png";
            case "snow" -> "snow.png";
            case "sleet" -> "sleet.png";
            case "thunderstorm" -> "thunderstorm.png";
            default -> "na.png";
        };
    }
}
