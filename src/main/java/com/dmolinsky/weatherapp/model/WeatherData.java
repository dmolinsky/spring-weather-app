package com.dmolinsky.weatherapp.model;

import java.time.LocalDateTime;

public class WeatherData {

    private String origin;
    private double temperature;
    private int humidity;
    private double precipitationChance;
    private double windSpeed;
    private LocalDateTime timestamp;
    private String iconFilename;
    private String styleId;

    public WeatherData(String origin, double temperature, int humidity,
                       double precipitationChance, double windSpeed,
                       LocalDateTime timestamp, String iconFilename, String styleId) {
        this.origin = origin;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitationChance = precipitationChance;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
        this.iconFilename = iconFilename;
        this.styleId = styleId;
    }

    public String getOrigin() {
        return origin;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getPrecipitationChance() {
        return precipitationChance;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIconFilename() {
        return iconFilename;
    }
    public String getStyleId() {
        return styleId;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setPrecipitationChance(double precipitationChance) {
        this.precipitationChance = precipitationChance;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setIconClass(String iconFilename) {
        this.iconFilename = iconFilename;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
}