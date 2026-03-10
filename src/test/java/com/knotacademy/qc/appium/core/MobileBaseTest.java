package com.knotacademy.qc.appium.core;

import com.knotacademy.qc.appium.pages.LoginPage;
import com.knotacademy.qc.appium.pages.ProductsPage;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.UnsupportedCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * MobileBaseTest: Clase base para todas las pruebas de Appium.
 *
 * Proporciona:
 * - Inicialización del driver Appium (@BeforeEach)
 * - Cierre del driver (@AfterEach)
 * - Configuración de timeouts
 * - Métodos auxiliares para las subclases
 *
 * Las subclases heredan de esta clase para obtener el driver automáticamente
 * en cada test.
 *
 * @author QA Automation Team
 * @version 1.0
 */
public abstract class MobileBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(MobileBaseTest.class);

    /**
     * Driver de Appium compartido para todas las pruebas
     */
    protected AppiumDriver driver;

    /**
     * Inicializa el driver Appium antes de cada prueba.
     *
     * - Crea una nueva instancia del driver
     * - Configura los timeouts implícitos y explícitos
     * - Registra en el log la inicialización
     */
    @BeforeEach
    public void setUp() {
        logger.info("=== Inicializando test ===");
        driver = DriverFactory.createDriver();
        configureTimeouts();
        logger.info("Driver inicializado correctamente");
    }

    /**
     * Cierra el driver Appium después de cada prueba.
     *
     * Asegura la liberación de recursos incluso si la prueba falla.
     */
    @AfterEach
    public void tearDown() {
        logger.info("=== Finalizando test ===");
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver cerrado correctamente");
            } catch (Exception e) {
                logger.error("Error al cerrar el driver: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Configura los timeouts del driver.
     *
     * Lee el tiempo de espera desde la configuración (appium.properties)
     * con un valor por defecto de 15 segundos.
     */
    private void configureTimeouts() {
        int timeoutSeconds = ConfigLoader.getInt("timeout.seconds", 15);
        Duration timeout = Duration.ofSeconds(timeoutSeconds);

        driver.manage().timeouts().implicitlyWait(timeout);

        // Algunos drivers móviles no implementan script timeout (ej. UiAutomator2 en ciertas versiones de Appium)
        try {
            driver.manage().timeouts().scriptTimeout(timeout);
        } catch (UnsupportedCommandException e) {
            logger.warn("Script timeout no soportado por el driver actual: {}", e.getMessage());
        }

        logger.info("Timeouts configurados: {} segundos", timeoutSeconds);
    }

    /**
     * Obtiene una propiedad requerida de la configuración.
     *
     * Lanza una excepción si la propiedad no existe.
     *
     * @param key Clave de la propiedad
     * @return Valor de la propiedad
     * @throws RuntimeException Si la propiedad no existe
     */
    protected String requiredProperty(String key) {
        String value = ConfigLoader.get(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Propiedad requerida no encontrada: " + key);
        }
        return value;
    }

    /**
     * Obtiene una propiedad con valor por defecto.
     *
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto
     * @return Valor de la propiedad o el valor por defecto
     */
    protected String optionalProperty(String key, String defaultValue) {
        return ConfigLoader.get(key, defaultValue);
    }

    /**
     * Asegura que la prueba inicie en ProductsPage.
     *
     * Este repositorio asume un flujo base simple donde la app
     * abre directamente en la lista de productos.
     *
     * @return Instancia de ProductsPage ya cargada
     */
    protected ProductsPage ensureProductsPage() {
        ProductsPage productsPage = new ProductsPage(driver);
        if (!productsPage.isLoaded()) {
            throw new IllegalStateException("La app debe iniciar en ProductsPage para este repo básico.");
        }

        logger.info("La aplicación inició en ProductsPage");
        return productsPage;
    }

    /**
     * Indica si la pantalla de login está visible en el estado actual.
     *
     * @return true si LoginPage es visible
     */
    protected boolean isLoginPageVisible() {
        return new LoginPage(driver).isLoaded();
    }

    /**
     * Indica si la pantalla de productos está visible en el estado actual.
     *
     * @return true si ProductsPage es visible
     */
    protected boolean isProductsPageVisible() {
        return new ProductsPage(driver).isLoaded();
    }
}
