package com.sap.task.cities_processor.exception;

public class CityDataLoadException extends RuntimeException {
    public CityDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public CityDataLoadException(String message) {
        super(message);
    }
}
