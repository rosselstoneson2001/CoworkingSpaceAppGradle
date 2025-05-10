package com.example.repositories.impl;

import com.example.exceptions.DatabaseException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.DataStorage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStorageImpl<E> implements DataStorage<E> {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Path filePath;
    private final TypeReference<List<E>> typeReference;


    static { // Register the JavaTimeModule for Java 8 Date/Time support
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules(); // Registers any other modules available
    }

    public DataStorageImpl(String fileName, TypeReference<List<E>> typeReference) {
        Path path = Paths.get(fileName);
        this.filePath = path.isAbsolute() ? path : Paths.get("domain/src/main/resources/files", fileName);
        this.typeReference = typeReference;
    }


    @Override
    public void save(Collection<E> data) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            INTERNAL_LOGGER.info("Data successfully saved to {}", filePath);
        } catch (IOException e) {
            INTERNAL_LOGGER.error("Failed to save data to {}: {}", filePath, e.getMessage(), e);
            throw new DatabaseException(ErrorCodes.DATABASE_ERROR, "Failed to save data to file: " + filePath);
        }
    }

    @Override
    public List<E> load() {
        if (!Files.exists(filePath)) {
            INTERNAL_LOGGER.info("File not found: {}", filePath);
            return new ArrayList<>();
        }
        try {
            String json = Files.readString(filePath);
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            INTERNAL_LOGGER.error("Error loading reservations from {}: {}", filePath, e.getMessage(), e);
            throw new DatabaseException(ErrorCodes.DATABASE_ERROR, "Failed to load data from file: " + filePath);
        }
    }

}
