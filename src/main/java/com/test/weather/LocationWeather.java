package com.test.weather;

import lombok.Data;

import java.util.List;

@Data
public class LocationWeather {

    private float temperature;

    private float wind;
    private String description;

    private List<Forecast> forecast;
}
