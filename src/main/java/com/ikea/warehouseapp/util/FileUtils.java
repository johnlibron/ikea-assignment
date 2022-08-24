package com.ikea.warehouseapp.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    public static boolean isValidJsonFilePath(String filepath) {
        // TODO - Test file path in Linux/Unix
        return Files.exists(Paths.get(filepath)) && filepath.endsWith(".json");
    }
}
