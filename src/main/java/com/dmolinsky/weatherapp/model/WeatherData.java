package com.dmolinsky.weatherapp.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

    private String origin;
    private double temperature;
    private int humidity;
    private double precipitationChance;
    private double windSpeed;
    private LocalDateTime timestamp;


}