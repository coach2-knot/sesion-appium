package com.knotacademy.qc.appium.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ProductsPage: Página de catálogo de productos de SauceLabs Demo App.
 *
 * Proporciona métodos para:
 * - Obtener información de productos
 * - Navegar a detalles del producto
 * - Verificar el carrito
 * - Hacer scroll en la lista de productos
 *
 * @author QA Automation Team
 * @version 1.0
 */
public class ProductsPage extends MobileBasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductsPage.class);

    // TODO: Locator para el título "Products" de la página
    private static final By PRODUCTS_TITLE = By.id("com.saucelabs.mydemoapp.android:id/productTV");

    // TODO: Locator para cada item de producto en la lista
    // Busca un contenedor que represente cada producto (puede ser LinearLayout, card, etc.)
    private static final By PRODUCT_ITEMS = By.id("com.saucelabs.mydemoapp.android:id/titleTV");

    // TODO: Locator para el badge/ícono del carrito
    // Busca el elemento que muestra la cantidad de items en el carrito
    private static final By CART_BADGE = By.xpath("TODO_COMPLETAR");

    private static final String PRODUCT_TITLE_FORMAT = "//*[@content-desc='store item']//following-sibling::*//*[contains(text(), '%s')]";

    /**
     * Constructor de la página de productos.
     *
     * @param driver Driver de Appium
     */
    public ProductsPage(AppiumDriver driver) {
        super(driver);
        logger.info("ProductsPage inicializada");
    }

    /**
     * Verifica que la página de productos está cargada.
     *
     * @return true si el título "Products" es visible
     */
    public boolean isLoaded() {
        try {
            return isVisible(PRODUCTS_TITLE);
        } catch (Exception e) {
            logger.debug("ProductsPage no está cargada: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el título del producto en una posición específica.
     *
     * @param index Índice del producto (basado en 0)
     * @return Título del producto
     */
    public String getProductTitle(int index) {
        try {
            List<WebElement> products = driver.findElements(PRODUCT_ITEMS);
            if (index < 0 || index >= products.size()) {
                throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
            }

            WebElement product = products.get(index);
            String title = product.getText();
            logger.info("Título del producto en índice {}: {}", index, title);
            return title;
        } catch (Exception e) {
            logger.error("Error al obtener título del producto en índice {}: {}", index, e.getMessage());
            throw new RuntimeException("No se pudo obtener el título del producto", e);
        }
    }

    /**
     * Obtiene el número de productos visibles.
     *
     * @return Cantidad de productos
     */
    public int getProductCount() {
        try {
            List<WebElement> products = driver.findElements(PRODUCT_ITEMS);
            logger.info("Cantidad de productos encontrados: {}", products.size());
            return products.size();
        } catch (Exception e) {
            logger.error("Error al contar productos: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Hace clic en un producto por su índice.
     *
     * @param index Índice del producto (basado en 0)
     */
    public void tapProduct(int index) {
        try {
            List<WebElement> products = driver.findElements(PRODUCT_ITEMS);
            if (index < 0 || index >= products.size()) {
                throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
            }

            WebElement product = products.get(index);
            product.click();
            logger.info("Tap realizado en producto en índice: {}", index);
        } catch (Exception e) {
            logger.error("Error al hacer tap en producto {}: {}", index, e.getMessage());
            throw new RuntimeException("No se pudo hacer tap en el producto", e);
        }
    }

    /**
     * Hace clic en un producto por su nombre.
     *
     * Realiza scroll automático para encontrar el producto si no es visible.
     *
     * @param productName Nombre del producto
     */
    public void tapProductByName(String productName) {
        try {
            scrollToElement(productName);
            By productBy = By.xpath(String.format(PRODUCT_TITLE_FORMAT, productName));
            tap(productBy);
            logger.info("Tap realizado en producto: {}", productName);
        } catch (Exception e) {
            logger.error("Error al hacer tap en producto '{}': {}", productName, e.getMessage());
            throw new RuntimeException("No se pudo hacer tap en el producto: " + productName, e);
        }
    }

    /**
     * Obtiene el número de artículos en el carrito.
     *
     * @return Número de artículos en el carrito, o 0 si no hay badge visible
     */
    public int getCartBadgeCount() {
        try {
            if (isVisible(CART_BADGE)) {
                String badgeText = text(CART_BADGE);
                int count = Integer.parseInt(badgeText);
                logger.info("Artículos en carrito: {}", count);
                return count;
            }
        } catch (NumberFormatException e) {
            logger.warn("No se pudo parsear el número de artículos: {}", e.getMessage());
        } catch (Exception e) {
            logger.debug("Badge del carrito no visible: {}", e.getMessage());
        }
        return 0;
    }

    /**
     * Verifica si el carrito tiene artículos.
     *
     * @return true si hay artículos en el carrito
     */
    public boolean hasCartItems() {
        return getCartBadgeCount() > 0;
    }

    /**
     * Realiza scroll para encontrar un producto por nombre.
     *
     * @param productName Nombre del producto a buscar
     */
    public void scrollToProduct(String productName) {
        try {
            scrollToElement(productName);
            logger.info("Scroll realizado para encontrar producto: {}", productName);
        } catch (Exception e) {
            logger.error("Error al hacer scroll para encontrar producto '{}': {}", productName, e.getMessage());
            throw new RuntimeException("No se pudo hacer scroll al producto", e);
        }
    }

    /**
     * Abre el carrito de compras.
     */
    public void openCart() {
        try {
            tap(CART_BADGE);
            logger.info("Carrito abierto");
        } catch (Exception e) {
            logger.error("Error al abrir el carrito: {}", e.getMessage());
            throw new RuntimeException("No se pudo abrir el carrito", e);
        }
    }
}
