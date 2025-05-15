package com.dmolinsky.weatherapp.service;

import com.dmolinsky.weatherapp.model.WeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeatherService {

    public WeatherData getOptimalWeather() {
        String origin = "MockWeatherAPI";
        double temperature = 19.2;
        int humidity = 42;
        double precipitationChance = 0.0;
        double windSpeed = 2.5;
        LocalDateTime timestamp = LocalDateTime.now().plusHours(24);
        String iconFilename = mapSymbolToIconFile(1);

        return new WeatherData(origin, temperature, humidity, precipitationChance, windSpeed, timestamp, iconFilename);
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
            default -> null;
        };
    }
}