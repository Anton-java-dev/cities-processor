package com.sap.task.cities_processor.service;

import com.sap.task.cities_processor.model.City;

import java.util.List;

/**
 * Interface for loading city data from various sources.
 * <p>
 * Implementations of this interface should handle the logic for reading and parsing
 * city data (e.g., from CSV, JSON, etc.) and returning a list of valid {@link City} objects.
 */
public interface CityDataLoader {

    /**
     * Loads and parses city data from the specified file path.
     *
     * @param filePath the path to the file containing city data
     * @return a list of valid {@link City} objects parsed from the file
     * @throws com.sap.task.cities_processor.exception.CityDataLoadException if loading the file fails
     */
    List<City> loadCities(String filePath);
}
