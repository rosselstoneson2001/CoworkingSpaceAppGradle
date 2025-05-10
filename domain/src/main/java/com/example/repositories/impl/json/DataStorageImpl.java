package com.example.repositories.impl.json;

import com.example.exceptions.RepositoryException;
import com.example.exceptions.enums.RepositoryErrorCodes;
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

/**
 * Implementation of the {@link DataStorage} interface using JSON file-based storage.
 * <p>
 * This class provides functionality for saving and loading data to and from a file in JSON format.
 * It uses Jackson's ObjectMapper for data serialization and deserialization, and the JavaTimeModule
 * for handling Java 8 Date/Time types.
 * </p>
 *
 * @param <E> The type of entity to be saved and loaded.
 */
public class DataStorageImpl<E> implements DataStorage<E> {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Path filePath;
    private final TypeReference<List<E>> typeReference;


    static { // Register the JavaTimeModule for Java 8 Date/Time support
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules(); // Registers any other modules available
    }

    /**
     * Constructs a new {@link DataStorageImpl} instance.
     *
     * @param fileName The name of the file where data will be stored or loaded from.
     * @param typeReference The type reference to use for deserialization of data into the correct type.
     */
    public DataStorageImpl(String fileName, TypeReference<List<E>> typeReference) {
        Path path = Paths.get(fileName);
        this.filePath = path.isAbsolute() ? path : Paths.get("domain/src/main/resources/files", fileName);
        this.typeReference = typeReference;
    }

    /**
     * Saves a collection of entities to the file in JSON format.
     *
     * @param data The collection of entities to be saved.
     * @throws RepositoryException if there is an error saving the data to the file.
     */
    @Override
    public void save(Collection<E> data) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            INTERNAL_LOGGER.info("Data successfully saved to {}", filePath);
        } catch (IOException e) {
            INTERNAL_LOGGER.error("Failed to save data to {}: {}", filePath, e.getMessage(), e);
            throw new RepositoryException(RepositoryErrorCodes.DATA_PERSISTENCE_ERROR, "Failed to save data to JSON file: " + filePath);
        }
    }

    /**
     * Loads the data from the file and deserializes it into a list of entities.
     *
     * @return A list of entities loaded from the file.
     * @throws RepositoryException if there is an error loading the data from the file.
     */
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
            throw new RepositoryException(RepositoryErrorCodes.LOAD_FAILED, "Failed to load data from JSON file: " + filePath);
        }
    }

}
