package com.sap.task.cities_processor.service.impl;

import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;
import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    @Qualifier("csvCityLoader")
    private CityDataLoader csvCityLoader;
    @Mock
    @Qualifier("jsonCityLoader")
    private CityDataLoader jsonCityLoader;

    private CityServiceImpl cityService;
    private final String smallestCity = "A";
    private final String biggestCity = "Z";
    private final int biggestPopulation = 10000000;
    private final int smallestPopulation = 1;
    private final double biggestArea = 100000;
    private final double smallestArea = 1;

    private final List<City> mockCities = List.of(
            new City(smallestCity, biggestPopulation, 10),
            new City("F", smallestPopulation, biggestArea),
            new City(biggestCity, 500, smallestArea)
    );
    private final int lastIndex = mockCities.size() - 1;

    @BeforeEach
    void setUp() {
        cityService = new CityServiceImpl(csvCityLoader, jsonCityLoader, "cities.csv", "cities.json");
    }

    @Test
    void shouldSortCitiesByNameAsc() {
        when(csvCityLoader.loadCities(anyString())).thenReturn(mockCities);

        List<CityDto> result = cityService.getCities(DataFormat.CSV, SortingField.NAME, true);

        assertThat(result.get(0).name()).isEqualTo(smallestCity);
        assertThat(result.get(lastIndex).name()).isEqualTo(biggestCity);
    }

    @Test
    void shouldSortCitiesByAreaAsc() {
        when(csvCityLoader.loadCities(anyString())).thenReturn(mockCities);

        List<CityDto> result = cityService.getCities(DataFormat.CSV, SortingField.AREA, true);

        assertThat(result.get(0).area()).isEqualTo(smallestArea);
        assertThat(result.get(lastIndex).area()).isEqualTo(biggestArea);
    }

    @Test
    void shouldSortCitiesByPopulationAsc() {
        when(csvCityLoader.loadCities(anyString())).thenReturn(mockCities);

        List<CityDto> result = cityService.getCities(DataFormat.CSV, SortingField.POPULATION, true);

        assertThat(result.get(0).population()).isEqualTo(smallestPopulation);
        assertThat(result.get(lastIndex).population()).isEqualTo(biggestPopulation);
    }

    @Test
    void shouldSortCitiesByPopulationDesc() {
        when(jsonCityLoader.loadCities(anyString())).thenReturn(mockCities);

        List<CityDto> result = cityService.getCities(DataFormat.JSON, SortingField.POPULATION, false);

        assertThat(result.get(0).population()).isEqualTo(biggestPopulation);
        assertThat(result.get(lastIndex).population()).isEqualTo(smallestPopulation);
    }
}
