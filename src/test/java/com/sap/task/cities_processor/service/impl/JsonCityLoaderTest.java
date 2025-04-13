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
class JsonCityLoaderTest {
    private static final String INVALID_JSON_FILE_PATH = "data/invalid/cities.json";
    private static final String VALID_JSON_FILE_PATH = "data/valid/cities.json";

    @Autowired
    @Qualifier("jsonCityLoader")
    CityDataLoader jsonCityLoader;

    @Test
    void shouldLoadCitiesFromJson() {
        List<City> cities = jsonCityLoader.loadCities(VALID_JSON_FILE_PATH);

        assertEquals(2, cities.size());

        City paris = cities.get(0);
        assertEquals("Paris", paris.name());
        assertEquals(2148000, paris.population());
        assertEquals(105.4, paris.area());
    }

    @Test
    void shouldFilterOutAllInvalidJsonDataLeavingOne() {
        List<City> cities = jsonCityLoader.loadCities(INVALID_JSON_FILE_PATH);

        assertEquals(1, cities.size());
        City remaining = cities.get(0);
        assertEquals("valid", remaining.name());
        assertEquals(1000, remaining.population());
        assertEquals(10.0, remaining.area());
    }
}
