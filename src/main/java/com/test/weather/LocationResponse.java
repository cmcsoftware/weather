package com.test.weather;

import lombok.Data;

import java.util.List;

@Data
public class LocationResponse {

    private List<LocationTemperature> result;

}
