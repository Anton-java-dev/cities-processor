package com.sap.task.cities_processor.controller.dto;

import com.sap.task.cities_processor.model.City;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CityDtoTest {
    @Test
    void shouldCreateValidDtoFromCity() {
        int expectedPopulation = 3_769_000;
        double expectedArea = 900.0;
        String expectedName = "City";
        City city = new City(expectedName, expectedPopulation, expectedArea);

        CityDto dto = new CityDto(city);

        assertThat(dto.name()).isEqualTo(expectedName);
        assertThat(dto.population()).isEqualTo(expectedPopulation);
        assertThat(dto.area()).isEqualTo(expectedArea);
        assertThat(dto.density()).isEqualTo(expectedPopulation / expectedArea);
    }
}
