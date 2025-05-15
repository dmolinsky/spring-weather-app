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

        return new WeatherData(origin, temperature, humidity, precipitationChance, windSpeed, timestamp);
    }
}