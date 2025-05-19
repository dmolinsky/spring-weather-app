package com.dmolinsky.weatherapp.controller;

import com.dmolinsky.weatherapp.model.WeatherData;
import com.dmolinsky.weatherapp.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String showWeather(Model model) {
        WeatherData weather = weatherService.getOptimalWeather();
        model.addAttribute("weather", weather);
        return "index";
    }

}
