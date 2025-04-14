package com.sap.task.cities_processor.controller;


import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;
import com.sap.task.cities_processor.exception.CityDataLoadException;
import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/cities")
    public ResponseEntity<List<CityDto>> getCities(
            @RequestParam DataFormat dataFormat,
            @RequestParam(required = false, defaultValue = "NAME") SortingField sortBy,
            @RequestParam(required = false, defaultValue = "true") boolean isAsc,
            @RequestParam(required = false) String nameContains) {
        return ResponseEntity.ok(cityService.getCities(dataFormat, sortBy, isAsc, nameContains));
    }

    @PostMapping("/cities")
    public ResponseEntity<Void> addCity(@RequestBody CityDto cityDto) {
        City newCity = new City(cityDto);
        if (!newCity.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        cityService.addCity(newCity);
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(CityDataLoadException.class)
    public ResponseEntity<String> handleCityDataLoadException(CityDataLoadException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Data loading failed: " + ex.getMessage());
    }
}

