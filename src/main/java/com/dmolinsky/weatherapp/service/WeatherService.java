package com.dmolinsky.weatherapp.service;

import com.dmolinsky.weatherapp.client.SmhiWeatherClient;
import com.dmolinsky.weatherapp.client.YrWeatherClient;
import com.dmolinsky.weatherapp.model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WeatherService {

    private final SmhiWeatherClient smhiWeatherClient;
    private final YrWeatherClient yrWeatherClient;

    @Autowired
    public WeatherService(SmhiWeatherClient smhiWeatherClient, YrWeatherClient yrWeatherClient) {
        this.smhiWeatherClient = smhiWeatherClient;
        this.yrWeatherClient = yrWeatherClient;
    }

    public WeatherData getOptimalWeather() {
        WeatherData smhiData = smhiWeatherClient.getWeather();
        WeatherData yrData = yrWeatherClient.getWeather();

        double smhiDataScore = calculateScore(smhiData);
        double yrDataScore = calculateScore(yrData);

        return smhiDataScore > yrDataScore ? yrData : smhiData;

    }

    /**
     Calculates a weather score based on four parameters:
     temperature, humidity, precipitation chance, and wind speed.
     Ideal values are: 25°C for temperature, 45% for humidity,
     0% for precipitation, and as little wind as possible (max 3m/s).
     Each parameter contributes to a total score between 0 and 40.
     */
    private double calculateScore(WeatherData data) {
        double temperatureScore = 10 - Math.abs(25 - data.getTemperature());
        double humidityScore = 10 - Math.abs(45 - data.getHumidity()) * 0.2;
        double precipitationScore = Math.max(0, 10 - data.getPrecipitationChance() * 10);
        double windScore = Math.max(0, 3 - data.getWindSpeed());

        return temperatureScore + humidityScore + precipitationScore + windScore;
    }

    private String mapSymbolToStyleId(String iconFilename) {
        return switch (iconFilename) {
            case "day-sunny.png" -> "sunny";
            case "day-cloudy.png", "cloudy.png" -> "cloudy";
            case "showers.png", "rain.png" -> "rainy";
            case "storm-showers.png", "thunderstorm.png" -> "stormy";
            case "snow.png", "sleet.png" -> "snowy";
            default -> "neutral";
        };
    }

}