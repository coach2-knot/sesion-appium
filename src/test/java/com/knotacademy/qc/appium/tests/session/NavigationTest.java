package com.knotacademy.qc.appium.tests.session;

import com.knotacademy.qc.appium.core.MobileBaseTest;
import com.knotacademy.qc.appium.pages.ProductsPage;
import com.knotacademy.qc.appium.pages.ProductDetailPage;
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
 * NavigationTest: Pruebas de navegación entre pantallas (Session Única).
 *
 * Verifica que:
 * 1. Se puede navegar de la lista de productos al detalle
 * 2. Se puede volver de detalle a la lista
 * 3. La navegación preserva el estado correcto
 *
 * @author QA Automation Team
 * @version 1.0
 */
@Epic("Session Única - Appium Automation")
@Feature("Navigation")
public class NavigationTest extends MobileBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(NavigationTest.class);

    @Test
    @DisplayName("S5-201: Navegar a detalles del producto desde la lista")
    @Story("Product Navigation")
    @Description("Verifica que se puede hacer clic en un producto y ver sus detalles")
    public void testNavigateToProductDetail() {
        logger.info("=== Iniciando test: testNavigateToProductDetail ===");

        // Asegurar página de productos
        ProductsPage productsPage = ensureProductsPage();
        logger.info("ProductsPage disponible");

        // Ir a detalle del primer producto
        assertTrue(productsPage.isLoaded(), "Página de productos debe estar visible");
        int productCount = productsPage.getProductCount();
        assertTrue(productCount > 0, "Debe haber al menos un producto");

        // Tap en el primer producto
        productsPage.tapProduct(0);
        logger.info("Tap en primer producto");

        // Esperar que cargue la página de detalle
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(5));

        // Verificar que estamos en la página de detalle
        ProductDetailPage detailPage = new ProductDetailPage(driver);
        assertTrue(detailPage.isLoaded(), "Página de detalle del producto debe estar visible");
        assertNotNull(detailPage.getProductName(), "El producto debe tener nombre");

        logger.info("✓ Test completado: Navegación a detalle exitosa");
    }

    @Test
    @DisplayName("S5-202: Volver a la lista desde el detalle del producto")
    @Story("Product Navigation")
    @Description("Verifica que se puede volver a la lista de productos desde el detalle")
    public void testBackFromProductDetail() {
        logger.info("=== Iniciando test: testBackFromProductDetail ===");

        // Asegurar página de productos
        ProductsPage productsPage = ensureProductsPage();
        logger.info("ProductsPage disponible");

        // Ir a detalle
        productsPage.tapProduct(0);
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(5));

        // Verificar que estamos en detalle
        ProductDetailPage detailPage = new ProductDetailPage(driver);
        assertTrue(detailPage.isLoaded(), "Página de detalle debe estar visible");

        // Volver atrás
        detailPage.goBack();
        logger.info("Volviendo a la lista de productos");

        // Esperar
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(3));

        // Verificar que estamos de vuelta en la lista
        productsPage = new ProductsPage(driver);
        assertTrue(productsPage.isLoaded(), "Debe volver a la página de productos");

        logger.info("✓ Test completado: Navegación hacia atrás exitosa");
    }

    @Test
    @DisplayName("S5-203: Navegar entre múltiples productos")
    @Story("Product Navigation")
    @Description("Verifica que se puede navegar a varios productos secuencialmente")
    public void testNavigateMultipleProducts() {
        logger.info("=== Iniciando test: testNavigateMultipleProducts ===");

        ProductsPage productsPage = ensureProductsPage();
        logger.info("ProductsPage disponible");
        assertTrue(productsPage.isLoaded(), "Página de productos debe estar visible");

        int productCount = productsPage.getProductCount();
        int testCount = Math.min(2, productCount); // Probar mínimo 2 o máximo disponibles

        for (int i = 0; i < testCount; i++) {
            logger.info("Navegando al producto {}", i);

            // Tap en producto
            productsPage.tapProduct(i);
            driver.manage().timeouts()
                    .implicitlyWait(java.time.Duration.ofSeconds(5));

            // Verificar detalle
            ProductDetailPage detailPage = new ProductDetailPage(driver);
            assertTrue(detailPage.isLoaded(), "Página de detalle debe estar visible para producto " + i);

            String productName = detailPage.getProductName();
            logger.info("Producto {}: {}", i, productName);

            // Volver
            detailPage.goBack();
            driver.manage().timeouts()
                    .implicitlyWait(java.time.Duration.ofSeconds(3));

            // Verificar que volvimos a la lista
            productsPage = new ProductsPage(driver);
            assertTrue(productsPage.isLoaded(), "Debe estar de vuelta en lista para producto " + i);
        }

        logger.info("✓ Test completado: Navegación entre {} productos exitosa", testCount);
    }

    @Test
    @DisplayName("S5-204: Información del producto debe ser consistente")
    @Story("Product Information")
    @Description("Verifica que los datos del producto son consistentes al navegar")
    public void testProductInfoConsistency() {
        logger.info("=== Iniciando test: testProductInfoConsistency ===");

        // Obtener primer producto de la lista
        ProductsPage productsPage = ensureProductsPage();
        logger.info("ProductsPage disponible");
        assertTrue(productsPage.isLoaded(), "Página de productos debe estar visible");

        String listProductTitle = productsPage.getProductTitle(0);
        logger.info("Título del producto en lista: {}", listProductTitle);

        // Navegar a detalle
        productsPage.tapProduct(0);
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(5));

        // Verificar nombre en detalle
        ProductDetailPage detailPage = new ProductDetailPage(driver);
        assertTrue(detailPage.isLoaded(), "Página de detalle debe estar visible");

        String detailProductTitle = detailPage.getProductName();
        logger.info("Título del producto en detalle: {}", detailProductTitle);

        // Los títulos deben ser similares/consistentes
        assertTrue(detailProductTitle.contains(listProductTitle) || listProductTitle.contains(detailProductTitle),
                "Los títulos deben ser consistentes");

        logger.info("✓ Test completado: Información del producto es consistente");
    }
}
