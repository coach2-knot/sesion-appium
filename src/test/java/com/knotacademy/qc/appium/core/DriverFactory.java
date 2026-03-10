package com.knotacademy.qc.appium.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * DriverFactory: Factory para crear instancias de AppiumDriver.
 *
 * Soporta:
 * - Android (UiAutomator2)
 * - iOS (XCUITest)
 *
 * Lee las propiedades de configuración y crea el driver apropiado
 * basado en la plataforma especificada.
 *
 * @author QA Automation Team
 * @version 1.0
 */
public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    private static final String APPIUM_URL = "http://127.0.0.1:4723";
    private static final String PLATFORM_PROPERTY = "platform";
    private static final String PLATFORM_ANDROID = "android";
    private static final String PLATFORM_IOS = "ios";

    /**
     * Crea un AppiumDriver basado en la plataforma especificada.
     *
     * La plataforma puede definirse mediante:
     * 1. Propiedad del sistema: -Dplatform=android o -Dplatform=ios
     * 2. Archivo de propiedades appium.properties
     *
     * @return AppiumDriver configurado para la plataforma
     * @throws RuntimeException Si la plataforma es inválida
     */
    public static AppiumDriver createDriver() {
        String platform = getPlatform();
        logger.info("Creando driver para plataforma: {}", platform);

        return switch (platform.toLowerCase()) {
            case PLATFORM_ANDROID -> createAndroidDriver();
            case PLATFORM_IOS -> createIOSDriver();
            default -> throw new RuntimeException("Plataforma no soportada: " + platform);
        };
    }

    /**
     * Obtiene la plataforma desde propiedad del sistema o configuración.
     *
     * @return Plataforma (android o ios)
     */
    private static String getPlatform() {
        String sysPlatform = System.getProperty(PLATFORM_PROPERTY);
        if (sysPlatform != null && !sysPlatform.isEmpty()) {
            return sysPlatform;
        }
        return ConfigLoader.get(PLATFORM_PROPERTY, PLATFORM_ANDROID);
    }

    /**
     * Crea un AndroidDriver con capacidades UiAutomator2.
     *
     * Las capacidades se cargan desde android.properties y deben incluir:
     * - platformName: Android
     * - automationName: UiAutomator2
     * - deviceName: Nombre del dispositivo/emulador
     * - app: Ruta a la APK
     *
     * @return AndroidDriver configurado
     */
    private static AndroidDriver createAndroidDriver() {
        logger.info("Creando AndroidDriver con UiAutomator2");

        // Cargar propiedades específicas de Android
        ConfigLoader.loadProperties("android.properties");

        // Crear opciones
        UiAutomator2Options options = new UiAutomator2Options();

        // Capacidades básicas
        String platformName = ConfigLoader.get("platformName", "Android");
        String automationName = ConfigLoader.get("automationName", "UiAutomator2");
        String deviceName = ConfigLoader.get("deviceName", "Medium_Phone");
        String app = ConfigLoader.get("app", "apps/SauceLabs.apk");
        String appPackage = ConfigLoader.get("appPackage");
        String appActivity = ConfigLoader.get("appActivity");
        String appWaitActivity = ConfigLoader.get("appWaitActivity");

        options.setPlatformName(platformName);
        options.setAutomationName(automationName);
        options.setDeviceName(deviceName);
        options.setApp(app);

        if (appPackage != null && !appPackage.isBlank()) {
            options.setAppPackage(appPackage);
        }
        if (appActivity != null && !appActivity.isBlank()) {
            options.setAppActivity(appActivity);
        }
        if (appWaitActivity != null && !appWaitActivity.isBlank()) {
            options.setAppWaitActivity(appWaitActivity);
        }

        // Opciones adicionales
        options.setNoReset(false);
        options.setFullReset(false);

        try {
            URL url = new URL(APPIUM_URL);
            AndroidDriver driver = new AndroidDriver(url, options);
            logger.info("AndroidDriver creado exitosamente");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error en la URL del servidor Appium: " + e.getMessage(), e);
        }
    }

    /**
     * Crea un IOSDriver con capacidades XCUITest.
     *
     * Las capacidades se cargan desde ios.properties y deben incluir:
     * - platformName: iOS
     * - automationName: XCUITest
     * - deviceName: Nombre del dispositivo
     * - platformVersion: Versión de iOS
     * - app: Ruta a la aplicación
     *
     * @return IOSDriver configurado
     */
    private static IOSDriver createIOSDriver() {
        logger.info("Creando IOSDriver con XCUITest");

        // Cargar propiedades específicas de iOS
        ConfigLoader.loadProperties("ios.properties");

        // Crear opciones
        XCUITestOptions options = new XCUITestOptions();

        // Capacidades básicas
        String platformName = ConfigLoader.get("platformName", "iOS");
        String automationName = ConfigLoader.get("automationName", "XCUITest");
        String deviceName = ConfigLoader.get("deviceName", "iPhone 15");
        String platformVersion = ConfigLoader.get("platformVersion", "17.2");
        String app = ConfigLoader.get("app", "apps/SauceLabs.app");

        options.setPlatformName(platformName);
        options.setAutomationName(automationName);
        options.setDeviceName(deviceName);
        options.setPlatformVersion(platformVersion);
        options.setApp(app);

        // Opciones adicionales
        options.setNoReset(false);
        options.setFullReset(false);

        try {
            URL url = new URL(APPIUM_URL);
            IOSDriver driver = new IOSDriver(url, options);
            logger.info("IOSDriver creado exitosamente");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error en la URL del servidor Appium: " + e.getMessage(), e);
        }
    }
}
