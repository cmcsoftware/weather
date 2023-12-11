package com.test.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<LocationResponse> getWeatherValues(@RequestParam("city") String city) {
        try {
            return ResponseEntity.ok(weatherService.getWeather(city));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
