package com.dmolinsky.weatherapp.service;

import com.dmolinsky.weatherapp.client.SmhiWeatherClient;
import com.dmolinsky.weatherapp.model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WeatherService {

    private final SmhiWeatherClient smhiWeatherClient;

    @Autowired
    public WeatherService(SmhiWeatherClient smhiWeatherClient) {
        this.smhiWeatherClient = smhiWeatherClient;
    }

    public WeatherData getOptimalWeather() {
        WeatherData smhiData = smhiWeatherClient.getWeather();

        return smhiData;
    }

}