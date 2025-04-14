package com.sap.task.cities_processor.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.task.cities_processor.model.City;

public record CityDto(
        String name,
        int population,
        double area,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        double density
) {
    public CityDto(City city) {
        this(city.name(), city.population(), city.area(), city.population() / city.area());
    }
}