package com.sap.task.cities_processor.service.impl;

import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class CsvCityLoaderImplTest {
    private static final String INVALID_CSV_FILE_PATH = "data/invalid/cities.csv";
    private static final String VALID_CSV_FILE_PATH = "data/valid/cities.csv";

    @Autowired
    @Qualifier("csvCityLoader")
    CityDataLoader csvCityLoader;

    @Test
    void shouldLoadCitiesFromCsv() {
        List<City> cities = csvCityLoader.loadCities(VALID_CSV_FILE_PATH);

        assertEquals(2, cities.size());

        City berlin = cities.get(0);
        assertEquals("Berlin", berlin.name());
        assertEquals(3769000, berlin.population());
        assertEquals(891.8, berlin.area());
    }

    @Test
    void shouldFilterOutAllInvalidCsvDataLeavingOne() {
        List<City> cities = csvCityLoader.loadCities(INVALID_CSV_FILE_PATH);

        assertEquals(1, cities.size());
        City remaining = cities.get(0);
        assertEquals("valid", remaining.name());
        assertEquals(1000, remaining.population());
        assertEquals(10.0, remaining.area());
    }
}

