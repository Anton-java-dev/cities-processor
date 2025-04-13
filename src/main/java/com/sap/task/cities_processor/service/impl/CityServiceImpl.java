package com.sap.task.cities_processor.service.impl;

import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;
import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityDataLoader;
import com.sap.task.cities_processor.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class CityServiceImpl implements CityService {

    public final CityDataLoader csvCityLoader;
    public final CityDataLoader jsonCityLoader;
    private final String csvFilePath;
    private final String jsonFilePath;

    public CityServiceImpl(
            @Qualifier("csvCityLoader") CityDataLoader csvCityLoader,
            @Qualifier("jsonCityLoader") CityDataLoader jsonCityLoader,
            @Value("${path.csv}") String csvFilePath,
            @Value("${path.json}") String jsonFilePath
    ) {
        this.csvCityLoader = csvCityLoader;
        this.jsonCityLoader = jsonCityLoader;
        this.csvFilePath = csvFilePath;
        this.jsonFilePath = jsonFilePath;
    }

    public List<CityDto> getCities(DataFormat dataFormat, SortingField sortBy, boolean isAsc) {
        List<City> cities = loadCities(dataFormat);
        return cities.stream()
                .sorted(createComparator(sortBy, isAsc))
                .map(CityDto::new)
                .toList();
    }

    private List<City> loadCities(DataFormat dataFormat) {
        return switch (dataFormat) {
            case CSV -> csvCityLoader.loadCities(csvFilePath);
            case JSON -> jsonCityLoader.loadCities(jsonFilePath);
        };
    }

    private Comparator<City> createComparator(SortingField sortBy, boolean isAsc) {
        Comparator<City> comparator =
                switch (sortBy) {
                    case NAME -> Comparator.comparing(City::name);
                    case POPULATION -> Comparator.comparingInt(City::population);
                    case AREA -> Comparator.comparingDouble(City::area);
                };

        return isAsc ? comparator : comparator.reversed();
    }
}
