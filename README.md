# Spring Weather App – REST-based Weather Comparison Tool

This project was developed as part of the *Web Services* course and demonstrates the use of Java and Spring Boot to consume external REST APIs, process weather forecast data, and present the most favorable weather conditions in a dynamic web interface.

## Purpose

The goal of the application is to fetch weather data from different public APIs and determine which forecast offers the "best" weather for the next 24 hours. The application is built using:
- Spring Boot
- Thymeleaf
- Maven
- WebClient (non-blocking HTTP client)

---

## Consumed APIs

The application integrates with the following external weather APIs:

| Source | API | Description |
|--------|-----|-------------|
| **SMHI** (Swedish Meteorological and Hydrological Institute) | `https://opendata-download-metfcst.smhi.se/` | Real-time 24-hour weather forecast data for a given coordinate |
| **Yr / MET Norway** | `https://api.met.no/weatherapi/locationforecast/2.0/compact` | Hourly forecast data provided by the Norwegian Meteorological Institute |

Each API is accessed through a dedicated service class that maps the provider’s data format to a common internal model (`WeatherData`).

---

## Weather Scoring Algorithm

When multiple sources are available, the application determines the optimal forecast using a **point-based scoring algorithm** that favors mild and pleasant weather conditions. Each weather property contributes a score, and the highest total wins:

| Parameter         | Scoring Rule                                          |
|------------------|--------------------------------------------------------|
| Temperature       | `10 − |25 − temperature|` → Ideal is 25 °C            |
| Humidity          | `10 − |45 − humidity| × 0.2` → Ideal is 45%           |
| Precipitation     | `max(0, 10 − precipitation × 10)` → Less is better    |
| Wind Speed        | `max(0, 3 − windSpeed)` → Lower wind gets more points |

> Example: A day with 25°C, 45% humidity, 0% rain, and 0 m/s wind would score the maximum of **40 points**.

---

## Weather Presentation

The final result is rendered in a responsive HTML view using Thymeleaf. The following is dynamically displayed:
- Temperature, humidity, wind, and time of forecast
- An icon representing the weather condition (e.g., sunny, rain, snow)
- A background color that changes depending on the weather type:
  - `sunny` – clear sky
  - `cloudy` – overcast or foggy
  - `rainy` – light or moderate rain
  - `stormy` – heavy rain or thunderstorms
  - `snowy` – snow or sleet

---

## Technologies Used

- Java 17+
- Spring Boot 3
- WebClient (non-blocking)
- Thymeleaf
- HTML & CSS
