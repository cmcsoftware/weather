package com.test.weather;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class WeatherService {


    private static final String WEATHER_API_URL = "https://8a5b7f27-5ae0-4fe2-9f31-ed20df36afa4.mock.pstmn.io/";

    public LocationResponse getWeather(String cityValues) {
        List<String> cities = Arrays.stream(cityValues.split(",")).map(String::trim).toList();
        LocationResponse response = new LocationResponse();
        List<LocationTemperature> locations = new ArrayList<>();

        for (String city : cities) {
            locations.add(getForecastForCity(city));
        }
        locations = locations.stream().sorted(Comparator.comparing(LocationTemperature::getName)).collect(Collectors.toList());
        response.setResult(locations);
        saveCsvContent(locations);

        return response;
    }

    private LocationTemperature getForecastForCity(String city) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<LocationWeather> responseEntity = restTemplate.getForEntity(WEATHER_API_URL + city, LocationWeather.class);

            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                List<Forecast> forecastList = responseEntity.getBody().getForecast();
                if (!forecastList.isEmpty()) {
                    Double averageTemperature = forecastList.stream().mapToDouble(Forecast::getTemperature).average().isPresent() ? forecastList.stream().mapToDouble(Forecast::getTemperature).average().getAsDouble() : 0;
                    Double averageWind = forecastList.stream().mapToDouble(Forecast::getWind).average().isPresent() ? forecastList.stream().mapToDouble(Forecast::getWind).average().getAsDouble() : 0;
                    return new LocationTemperature(city, String.format("%.2f", averageTemperature), String.format("%.2f", averageWind));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new LocationTemperature(city, "", "");
    }

    private void saveCsvContent(List<LocationTemperature> values) {
        StringBuilder csvContent = new StringBuilder("Name, Temperature, Wind\n");

        values.forEach(e ->
                csvContent.append(e.getName()).append(",").append(e.getTemperature()).append(",").append(e.getWind()).append("\n")
        );

        try {
            Path path = Paths.get("export.csv");
            Files.write(path, csvContent.toString().getBytes());
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }

    }


}
