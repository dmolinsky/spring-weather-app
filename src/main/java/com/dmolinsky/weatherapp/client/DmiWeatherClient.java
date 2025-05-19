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
public class DmiWeatherClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    String apiKey = System.getenv("DMI_API_KEY");

    public DmiWeatherClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://dmigw.govcloud.dk/v1/forecastedr")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("X-Gravitee-Api-Key", apiKey)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public WeatherData getWeather() {

        LocalDateTime targetTime = LocalDateTime.now().plusHours(24).withMinute(0).withSecond(0).withNano(0);
        String targetTimeStr = targetTime.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";

        String urlPath = String.format("/collections/harmonie_dini_sf/position" +
                "?coords=POINT(18.0275596 59.3042913)" +
                "&crs=crs84" +
                "&parameter-name=temperature-2m,relative-humidity-2m,total-precipitation,wind-speed-10m" +
                "&datetime=" + targetTimeStr +
                "&f=CoverageJSON");


        String json = webClient.get()
                .uri(urlPath)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode ranges = root.get("ranges");
            if (ranges == null) {
                throw new RuntimeException("No 'ranges' found in response");
            }

            double temperatureKelvin = ranges.get("temperature-2m").get("values").get(0).asDouble();
            int humidity = (int) Math.round(ranges.get("relative-humidity-2m").get("values").get(0).asDouble());
            double windSpeed = ranges.get("wind-speed-10m").get("values").get(0).asDouble();
            double precipitation = ranges.get("total-precipitation").get("values").get(0).asDouble();

            double temperatureCelsius = temperatureKelvin - 273.15;

            String iconFilename = mapWeatherToIconFile(temperatureCelsius, humidity, windSpeed, precipitation);
            String styleId = mapSymbolToStyleId(iconFilename);

            return new WeatherData("DMI", temperatureCelsius, humidity, precipitation, windSpeed, targetTime, iconFilename, styleId);

        } catch (IOException e) {
            throw new RuntimeException("Could not parse JSON from DMI", e);
        }
    }

    private String mapWeatherToIconFile(double temperature, int humidity, double precipitationChance, double windSpeed) {
        if (precipitationChance > 0.4) {
            if (temperature <= 0) {
                return "snow.png";
            } else if (precipitationChance > 0.7) {
                return "rain.png";
            } else {
                return "rain.png";
            }
        }

        if (windSpeed > 12) {
            return "thunderstorm.png";
        }

        if (humidity > 80) {
            return "cloudy.png";
        }

        if (temperature > 20) {
            return "day-sunny.png";
        } else if (temperature > 15) {
            return "day-cloudy.png";
        } else return "cloudy.png";
    }

    private String mapSymbolToStyleId(String iconFilename) {
        return switch (iconFilename) {
            case "day-sunny.png", "night-clear.png" -> "sunny";
            case "day-cloudy.png", "night-alt-cloudy.png", "cloudy.png" -> "cloudy";
            case "rain.png" -> "rainy";
            case "snow.png" -> "snowy";
            case "thunderstorm.png" -> "stormy";
            default -> "neutral";
        };
    }
}
