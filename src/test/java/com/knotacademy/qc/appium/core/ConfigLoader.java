package com.knotacademy.qc.appium.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigLoader: Cargador de propiedades de configuración para las pruebas de Appium.
 *
 * Lee archivos de propiedades desde resources para obtener:
 * - URL del servidor Appium
 * - Plataforma (Android/iOS)
 * - Tiempos de espera
 * - Capacidades específicas del dispositivo
 *
 * @author QA Automation Team
 * @version 1.0
 */
public class ConfigLoader {

    private static final Properties properties = new Properties();
    private static final String DEFAULT_PROPERTIES = "appium.properties";
    private static boolean loaded = false;

    static {
        loadDefaultProperties();
    }

    /**
     * Carga las propiedades por defecto desde appium.properties
     */
    private static void loadDefaultProperties() {
        if (!loaded) {
            try (InputStream input = ConfigLoader.class.getClassLoader()
                    .getResourceAsStream(DEFAULT_PROPERTIES)) {
                if (input == null) {
                    throw new RuntimeException("Archivo de propiedades no encontrado: " + DEFAULT_PROPERTIES);
                }
                properties.load(input);
                loaded = true;
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar las propiedades: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Carga propiedades adicionales desde un archivo específico.
     *
     * @param filename Nombre del archivo de propiedades
     */
    public static void loadProperties(String filename) {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("Archivo de propiedades no encontrado: " + filename);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar propiedades desde " + filename + ": " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el valor de una propiedad.
     *
     * @param key Clave de la propiedad
     * @return Valor de la propiedad, o null si no existe
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Obtiene el valor de una propiedad con valor por defecto.
     *
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto si la propiedad no existe
     * @return Valor de la propiedad o el valor por defecto
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Obtiene una propiedad como entero.
     *
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto
     * @return Valor convertido a entero
     */
    public static int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Obtiene una propiedad como booleano.
     *
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto
     * @return Valor convertido a booleano
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Reinicia la configuración (útil para pruebas).
     */
    public static void reset() {
        properties.clear();
        loaded = false;
        loadDefaultProperties();
    }
}
