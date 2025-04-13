package com.sap.task.cities_processor.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.task.cities_processor.exception.CityDataLoadException;
import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityDataLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service("jsonCityLoader")
@RequiredArgsConstructor
public class JsonCityLoader implements CityDataLoader {
    private final ObjectMapper objectMapper;

    @Override
    public List<City> loadCities(String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            throw new CityDataLoadException("JSON file not found at path: " + filePath);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            List<City> parsedCities = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            return parsedCities.stream()
                    .filter(City::isValid)
                    .toList();

        } catch (Exception e) {
            log.error("Failed to load cities from JSON at path: {}", filePath, e);
            throw new CityDataLoadException("Failed to load cities from JSON", e);
        }
    }


}
