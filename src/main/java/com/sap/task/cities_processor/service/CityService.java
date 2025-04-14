package com.sap.task.cities_processor.service;

import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;
import com.sap.task.cities_processor.model.City;

import java.util.List;

/**
 * Service interface for processing and retrieving city data.
 */
public interface CityService {

    /**
     * Retrieves a list of cities from the specified data format and returns them sorted by the given field and direction.
     *
     * @param dataFormat   the format of the data source (e.g., CSV, JSON)
     * @param sortBy       the field to sort the cities by (e.g., NAME, POPULATION, AREA)
     * @param isAsc        true for ascending order, false for descending
     * @param nameContains optional substring to filter city names
     * @return a list of {@link CityDto} objects representing the city data
     */
    List<CityDto> getCities(DataFormat dataFormat, SortingField sortBy, boolean isAsc, String nameContains);

    /**
     * Adds a new city to the in-memory collection of runtime-added cities.
     * <p>
     * These cities will be included in the results of {@code getCities()} calls,
     * but are not persisted to the original CSV or JSON files.
     *
     * @param city the {@link City} object to be added at runtime
     */
    void addCity(City city);
}

