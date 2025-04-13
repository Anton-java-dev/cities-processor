package com.sap.task.cities_processor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.task.cities_processor.controller.dto.CityDto;
import com.sap.task.cities_processor.controller.dto.DataFormat;
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
    private final static String URL_CITIES = "/api/cities";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnCitiesFromCsv() throws Exception {
        MvcResult result = mockMvc.perform(get(URL_CITIES).param("dataFormat", DataFormat.CSV.name()))
                .andExpect(status().isOk())
                .andReturn();

        List<CityDto> cities = getCitiesFromResponse(result);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Berlin");
        assertThat(cities.get(1).population()).isEqualTo(1841000);
    }

    @Test
    void shouldReturnCitiesFromJson() throws Exception {
        MvcResult result = mockMvc.perform(get(URL_CITIES).param("dataFormat", DataFormat.JSON.name()))
                .andExpect(status().isOk())
                .andReturn();

        List<CityDto> cities = getCitiesFromResponse(result);

        assertThat(cities).hasSize(2);
        assertThat(cities.get(0).name()).isEqualTo("Paris");
        assertThat(cities.get(1).population()).isEqualTo(515695);
    }

    @Test
    void shouldReturnBadRequestForInvalidDataFormat() throws Exception {
        mockMvc.perform(get(URL_CITIES).param("dataFormat", "XML"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDataFormatMissing() throws Exception {
        mockMvc.perform(get(URL_CITIES))
                .andExpect(status().isBadRequest());
    }

    private List<CityDto> getCitiesFromResponse(MvcResult result) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
    }
}
