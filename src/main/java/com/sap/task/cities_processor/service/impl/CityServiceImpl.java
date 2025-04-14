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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Slf4j
@Service
public class CityServiceImpl implements CityService {

    public final CityDataLoader csvCityLoader;
    public final CityDataLoader jsonCityLoader;
    private final String csvFilePath;
    private final String jsonFilePath;
    private final List<City> addedCities = new CopyOnWriteArrayList<>();

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

    public List<CityDto> getCities(DataFormat dataFormat, SortingField sortBy, boolean isAsc, String nameContains) {
        List<City> cities = loadCities(dataFormat);
        return Stream.of(cities, addedCities)
                .flatMap(Collection::stream)
                .filter(city -> cityNameContainsFilter(city.name(), nameContains))
                .sorted(createComparator(sortBy, isAsc))
                .map(CityDto::new)
                .toList();
    }

    @Override
    public void addCity(City city) {
        log.info("Added city {}", city.name());
        addedCities.add(city);
    }

    private boolean cityNameContainsFilter(String cityName, String nameContains) {
        if (nameContains == null || nameContains.isBlank()) {
            return true;
        }
        return cityName != null && cityName.toLowerCase().contains(nameContains.toLowerCase());
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
