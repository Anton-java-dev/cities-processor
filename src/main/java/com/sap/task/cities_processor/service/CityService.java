package com.sap.task.cities_processor.service;

import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;

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
}

