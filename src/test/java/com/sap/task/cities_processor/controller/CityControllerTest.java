package com.sap.task.cities_processor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
import com.sap.task.cities_processor.controller.dto.SortingField;
import com.sap.task.cities_processor.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CityControllerTest {
    private static final String URL_CITIES = "/api/cities";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldSortCitiesByNameAscFromCsv() throws Exception {
        List<CityDto> cities = performSortRequest(DataFormat.CSV, SortingField.NAME, true);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Berlin");
        assertThat(cities.get(1).name()).isEqualTo("Hamburg");
    }

    @Test
    void shouldSortCitiesByNameDescFromJson() throws Exception {
        List<CityDto> cities = performSortRequest(DataFormat.JSON, SortingField.NAME, false);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Paris");
        assertThat(cities.get(1).name()).isEqualTo("Lyon");
    }

    @Test
    void shouldSortCitiesByPopulationAscFromJson() throws Exception {
        List<CityDto> cities = performSortRequest(DataFormat.JSON, SortingField.POPULATION, true);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Lyon");
        assertThat(cities.get(1).name()).isEqualTo("Paris");
    }

    @Test
    void shouldSortCitiesByAreaDescFromCsv() throws Exception {
        List<CityDto> cities = performSortRequest(DataFormat.CSV, SortingField.AREA, false);

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

    @Test
    void shouldReturnCityWithNameContainingSubstringFromCsv() throws Exception {
        List<CityDto> cities = performContainsFilterRequest(DataFormat.CSV, "ham");

        assertThat(cities).hasSize(1);
        assertThat(cities.get(0).name()).isEqualTo("Hamburg");
    }

    @Test
    void shouldReturnCityWithNameContainingSubstringFromJson() throws Exception {
        List<CityDto> cities = performContainsFilterRequest(DataFormat.JSON, "yon");

        assertThat(cities).hasSize(1);
        assertThat(cities.get(0).name()).isEqualTo("Lyon");
    }

    @Test
    void shouldAddNewCityAndReturnItInList() throws Exception {
        List<CityDto> initialCities = performRequestDefaultSort(DataFormat.CSV);
        int initialCount = initialCities.size();
        String name = "Tokyo";

        addTestCity("Tokyo", 13_960_000, 2194.0, HttpStatus.OK.value());

        List<CityDto> updatedCities = performRequestDefaultSort(DataFormat.CSV);
        assertThat(updatedCities).hasSize(initialCount + 1);
        assertThat(updatedCities)
                .anyMatch(c -> name.equals(c.name()));
    }

    @Test
    void shouldRejectInvalidCity() throws Exception {
        List<CityDto> initialCities = performRequestDefaultSort(DataFormat.CSV);
        int initialCount = initialCities.size();

        addTestCity("", -1, 0, HttpStatus.BAD_REQUEST.value());

        List<CityDto> updatedCities = performRequestDefaultSort(DataFormat.CSV);
        assertThat(updatedCities).size().isEqualTo(initialCount);
    }

    @Test
    void shouldFilterAddedCitiesByName() throws Exception {
        addTestCity("New York", 8_500_000, 783.8, HttpStatus.OK.value());
        addTestCity("New Orleans", 390_000, 906.1, HttpStatus.OK.value());

        // Test filtering
        List<CityDto> filteredCities = performContainsFilterRequest(DataFormat.CSV, "new");

        assertThat(filteredCities)
                .hasSize(2)
                .extracting(CityDto::name)
                .containsExactlyInAnyOrder("New York", "New Orleans");
    }

    private void addTestCity(String name, int population, double area, int expectedStatus) throws Exception {
        City city = new City(name, population, area);
        mockMvc.perform(post(URL_CITIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(city)))
                .andExpect(expectedStatus == 200 ? status().isOk() : status().isBadRequest());
    }

    private static ResultMatcher getOk() {
        return status().isOk();
    }

    private List<CityDto> performSortRequest(DataFormat format, SortingField field, boolean asc) throws Exception {
        MvcResult result = mockMvc.perform(get(URL_CITIES)
                        .param("dataFormat", format.name())
                        .param("sortField", field.name())
                        .param("isAsc", Boolean.toString(asc)))
                .andExpect(status().isOk())
                .andReturn();

        return getCitiesFromResponse(result);
    }

    private List<CityDto> performContainsFilterRequest(DataFormat format, String nameContains) throws Exception {
        MvcResult result = mockMvc.perform(get(URL_CITIES)
                        .param("dataFormat", format.name())
                        .param("nameContains", nameContains))
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
