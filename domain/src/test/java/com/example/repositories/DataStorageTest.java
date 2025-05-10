package com.example.repositories;

import com.example.exceptions.DatabaseException;
import com.example.repositories.impl.DataStorageImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataStorageTest {


    private DataStorageImpl<String> dataStorage;
    private Path testFilePath;
    private final TypeReference<List<String>> typeReference = new TypeReference<>() {};

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        testFilePath = tempDir.resolve("test-data.json");
        dataStorage = new DataStorageImpl<>(testFilePath.toString(), typeReference);
    }

    @Test
    void testSave() throws IOException {
        List<String> testData = Arrays.asList("one", "two", "three");
        dataStorage.save(testData);

        assertTrue(Files.exists(testFilePath), "File should exist after saving");
        String content = Files.readString(testFilePath);
        assertFalse(content.isEmpty(), "File content should not be empty");
    }

    @Test
    void testLoad() {
        List<String> testData = Arrays.asList("alpha", "beta", "gamma");
        dataStorage.save(testData);
        List<String> loadedData = dataStorage.load();

        assertEquals(testData, loadedData, "Loaded data should match saved data");
    }

    @Test
    void testLoadFileNotFound() {
        List<String> loadedData = dataStorage.load();
        assertTrue(loadedData.isEmpty(), "Should return empty list when file is not found");
    }

    @Test
    void testLoadInvalidJson() throws IOException {
        Files.writeString(testFilePath, "invalid json content");

        assertThrows(DatabaseException.class, () -> dataStorage.load(), "Should throw exception on invalid JSON");
    }

}
