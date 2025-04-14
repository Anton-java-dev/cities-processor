package com.sap.task.cities_processor.service.impl;

import com.sap.task.cities_processor.exception.CityDataLoadException;
import com.sap.task.cities_processor.model.City;
import com.sap.task.cities_processor.service.CityDataLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("csvCityLoader")
@RequiredArgsConstructor
public class CsvCityLoaderImpl implements CityDataLoader {
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POPULATION = "population";
    private static final String COLUMN_AREA = "area";

    private final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setSkipHeaderRecord(true)
            .setIgnoreSurroundingSpaces(true)
            .setHeader()
            .build();

    public List<City> loadCities(String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            throw new CityDataLoadException("CSV file not found at path: " + filePath);
        }

        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVParser parser = csvFormat.parse(reader)) {

            List<City> cities = new ArrayList<>();
            for (CSVRecord record : parser) {
                parseRecord(record)
                        .filter(City::isValid)
                        .ifPresent(cities::add);
            }
            return cities;

        } catch (Exception e) {
            log.error("Failed to load cities from CSV at path: {}", filePath, e);
            throw new CityDataLoadException("Failed to load cities from CSV", e);
        }
    }

    private Optional<City> parseRecord(CSVRecord record) {
        try {
            String name = record.get(COLUMN_NAME).trim();
            int population = Integer.parseInt(record.get(COLUMN_POPULATION));
            double area = Double.parseDouble(record.get(COLUMN_AREA));

            return Optional.of(new City(name, population, area));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid city record skipped (Record {}): {}", record.getRecordNumber(), e.getMessage());
            return Optional.empty();
        }
    }
}
