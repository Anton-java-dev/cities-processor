package com.sap.task.cities_processor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void shouldCreateValidCity() {
        City city = new City("ValidCity", 1000, 10.5);
        assertEquals("ValidCity", city.name());
        assertTrue(city.isValid());
    }

    @Test
    void shouldMarkCityInvalidDueToName() {
        City city = new City("", 1000, 50);
        assertFalse(city.isValid());
    }

    @Test
    void shouldMarkCityInvalidDueToPopulation() {
        City city = new City("Test", 0, 50);
        assertFalse(city.isValid());
    }

    @Test
    void shouldMarkCityInvalidDueToArea() {
        City city = new City("Test", 1000, -10);
        assertFalse(city.isValid());
    }
}
