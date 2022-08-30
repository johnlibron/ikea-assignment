package com.ikea.warehouseapp.util;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtils {

    public static File getJsonFile(String filepath) throws FileNotFoundException {
        // TODO - Test file path in Linux/Unix
        File file = new File(filepath);
        String fileName = file.getName();
        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " does not exist");
        }
        if (!filepath.endsWith(".json")) {
            throw new FileNotFoundException("File " + fileName + " is not a JSON file");
        }
        return file;
    }
}
