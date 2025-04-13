package com.sap.task.cities_processor.controller;


import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.exception.CityDataLoadException;
import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityDataLoader;
import com.sap.task.cities_processor.service.impl.JsonCityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CityController {

    @Qualifier("csvCityLoader")
    public final CityDataLoader csvCityLoader;
    @Qualifier("jsonCityLoader")
    public final JsonCityLoader jsonCityLoader;

    @Value("${path.csv}")
    private String csvFilePath;
    @Value("${path.json}")
    private String jsonFilePath;

    @GetMapping("/cities")
    public ResponseEntity<List<CityDto>> getCities(@RequestParam DataFormat dataFormat) {
        List<City> cities = switch (dataFormat) {
            case CSV -> csvCityLoader.loadCities(csvFilePath);
            case JSON -> jsonCityLoader.loadCities(jsonFilePath);
        };

        List<CityDto> response = cities.stream()
                .map(CityDto::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(CityDataLoadException.class)
    public ResponseEntity<String> handleCityDataLoadException(CityDataLoadException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Data loading failed: " + ex.getMessage());
    }
}

