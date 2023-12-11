package com.test.weather;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationTemperature {

    private String name;

    private String temperature;

    private String wind;

}
