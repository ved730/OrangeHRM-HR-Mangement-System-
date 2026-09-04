package com.orangehrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads test configuration (URL, credentials, timeouts) from config.properties
 * so nothing is hardcoded inside test classes.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
