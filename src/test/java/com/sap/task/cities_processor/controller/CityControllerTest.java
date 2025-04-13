package com.sap.task.cities_processor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CityControllerTest {
    private static final String URL_CITIES = "/api/cities";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldSortCitiesByNameAscFromCsv() throws Exception {
        List<CityDto> cities = performRequest(DataFormat.CSV, SortingField.NAME, true);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Berlin");
        assertThat(cities.get(1).name()).isEqualTo("Hamburg");
    }

    @Test
    void shouldSortCitiesByNameDescFromJson() throws Exception {
        List<CityDto> cities = performRequest(DataFormat.JSON, SortingField.NAME, false);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Paris");
        assertThat(cities.get(1).name()).isEqualTo("Lyon");
    }

    @Test
    void shouldSortCitiesByPopulationAscFromJson() throws Exception {
        List<CityDto> cities = performRequest(DataFormat.JSON, SortingField.POPULATION, true);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Lyon");
        assertThat(cities.get(1).name()).isEqualTo("Paris");
    }

    @Test
    void shouldSortCitiesByAreaDescFromCsv() throws Exception {
        List<CityDto> cities = performRequest(DataFormat.CSV, SortingField.AREA, false);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Hamburg");
        assertThat(cities.get(1).name()).isEqualTo("Berlin");
    }

    @Test
    void shouldSortCitiesByDefaultSortFromCsv() throws Exception {
        List<CityDto> cities = performRequestDefaultSort(DataFormat.CSV);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Berlin");
        assertThat(cities.get(1).name()).isEqualTo("Hamburg");
    }

    private List<CityDto> performRequest(DataFormat format, SortingField field, boolean asc) throws Exception {
        MvcResult result = mockMvc.perform(get(URL_CITIES)
                        .param("dataFormat", format.name())
                        .param("sortField", field.name())
                        .param("isAsc", Boolean.toString(asc)))
                .andExpect(status().isOk())
                .andReturn();

        return getCitiesFromResponse(result);
    }

    private List<CityDto> performRequestDefaultSort(DataFormat format) throws Exception {
        MvcResult result = mockMvc.perform(get(URL_CITIES)
                        .param("dataFormat", format.name()))
                .andExpect(status().isOk())
                .andReturn();

        return getCitiesFromResponse(result);
    }

    private List<CityDto> getCitiesFromResponse(MvcResult result) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
    }
}
