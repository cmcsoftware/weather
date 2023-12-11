package com.test.weather;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Forecast {

    private int day;

    private float temperature;

    private float wind;
}
