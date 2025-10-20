package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFileReader {

    private final Properties properties;
    // Le nom du fichier est supposé se trouver dans src/test/resources de votre projet Maven/Gradle
    private static final String CONFIG_FILE_PATH = "config.properties";

    public ConfigFileReader() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (input == null) {
                // Le fichier n'a pas été trouvé dans le classpath
                throw new RuntimeException("Impossible de trouver le fichier de configuration dans le classpath: " + CONFIG_FILE_PATH);
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier de configuration : " + CONFIG_FILE_PATH);
            e.printStackTrace();
            throw new RuntimeException("Erreur de lecture du fichier de configuration.", e);
        }
    }

    public String getProperties(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("La clé '" + key + "' n'a pas été trouvée dans config.properties.");
        }
        return value;
    }
}