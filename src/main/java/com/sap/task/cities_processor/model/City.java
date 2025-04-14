package com.sap.task.cities_processor.model;

import com.sap.task.cities_processor.controller.dto.CityDto;

public record City(String name, int population, double area) {
    public boolean isValid() {
        return name != null && !name.trim().isEmpty()
                && population > 0
                && area > 0;
    }

    public City(CityDto cityDto) {
        this(cityDto.name(), cityDto.population(), cityDto.area());
    }
}

