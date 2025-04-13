package com.sap.task.cities_processor.model;

public record City(String name, int population, double area) {
    public boolean isValid() {
        return name != null && !name.trim().isEmpty()
                && population > 0
                && area > 0;
    }
}

