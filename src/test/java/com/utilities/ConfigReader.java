package com.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private Properties prop;
    private String proppath = "./src/test/resources/data/config.properties";

    public Properties loadConfig() {
        prop = new Properties();
        try (FileInputStream ip = new FileInputStream(proppath)) {
            prop.load(ip);
            logger.info("Loaded config.properties from path: {}", proppath);
        } catch (FileNotFoundException e) {
            logger.error("config.properties not found at {}: {}", proppath, e.getMessage());
            throw new RuntimeException("config.properties not found at " + proppath);
        } catch (IOException e) {
            logger.error("Error loading config.properties: {}", e.getMessage());
            throw new RuntimeException("Error loading config.properties: " + e.getMessage());
        }
        return prop;
    }

    public String getBrowser() {
        if (prop == null) {
            logger.warn("Properties object is null. Ensure config is loaded before accessing properties.");
            return null;
        }
        return prop.getProperty("browser");
    }

    public String getBrowserMode() {
        if (prop == null) {
            logger.warn("Properties object is null. Ensure config is loaded before accessing properties.");
            return null;
        }
        return prop.getProperty("mode");
    }
}
