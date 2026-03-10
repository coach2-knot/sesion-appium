package com.knotacademy.qc.appium.tests.session5;

import com.knotacademy.qc.appium.core.MobileBaseTest;
import com.knotacademy.qc.appium.pages.ProductsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LaunchTest: Pruebas de lanzamiento de la aplicación (Session 5).
 *
 * Verifica que:
 * 1. La aplicación se abre correctamente
 * 2. La pantalla de productos es visible al iniciar
 * 3. La pantalla de productos muestra artículos
 *
 * @author QA Automation Team
 * @version 1.0
 */
@Epic("Session 5 - Mobile App Fundamentals")
@Feature("App Launch and Navigation")
public class LaunchTest extends MobileBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LaunchTest.class);

    @Test
    @DisplayName("S5-001: La aplicación debe abrirse correctamente")
    @Story("App Initialization")
    @Description("Verifica que la aplicación se abre y muestra la pantalla de productos")
    public void testAppLaunchesSuccessfully() {
        logger.info("=== Iniciando test: testAppLaunchesSuccessfully ===");

        // Verificar que el driver está inicializado
        assertNotNull(driver, "El driver no debe ser null");
        logger.info("Driver inicializado correctamente");

        // Verificar que la pantalla de productos está visible
        ProductsPage productsPage = ensureProductsPage();
        assertTrue(productsPage.isLoaded(), "La pantalla de productos debe ser visible");

        logger.info("✓ Test completado: Aplicación se abrió correctamente");
    }

    @Test
    @DisplayName("S5-002: La pantalla de productos debe ser accesible al iniciar")
    @Story("App Initialization")
    @Description("Verifica que la pantalla de productos es accesible desde el inicio de la app")
    public void testProductsPageIsAccessibleOnLaunch() {
        logger.info("=== Iniciando test: testProductsPageIsAccessibleOnLaunch ===");

        // Verificar página de productos
        ProductsPage productsPage = ensureProductsPage();
        assertTrue(productsPage.isLoaded(), "La pantalla de productos debe ser visible");

        logger.info("✓ Test completado: Página de productos accesible");
    }

    @Test
    @DisplayName("S5-003: Verificar que hay productos en la pantalla")
    @Story("Products Display")
    @Description("Verifica que la página de productos muestra al menos un artículo al iniciar la app")
    public void testProductsDisplayed() {
        logger.info("=== Iniciando test: testProductsDisplayed ===");

        // Verificar productos
        ProductsPage productsPage = ensureProductsPage();
        assertTrue(productsPage.isLoaded(), "Página de productos debe estar visible");

        int productCount = productsPage.getProductCount();
        assertTrue(productCount > 0, "Debe haber al menos un producto en la pantalla");
        logger.info("Productos encontrados: {}", productCount);

        logger.info("✓ Test completado: {} productos visualizados", productCount);
    }
}
