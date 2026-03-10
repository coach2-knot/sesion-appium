package com.knotacademy.qc.appium.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProductDetailPage: Página de detalle de un producto en SauceLabs Demo App.
 *
 * Proporciona métodos para:
 * - Obtener información del producto
 * - Añadir producto al carrito
 * - Navegar de vuelta a la lista de productos
 *
 * @author QA Automation Team
 * @version 1.0
 */
public class ProductDetailPage extends MobileBasePage {

    private static final Logger logger = LoggerFactory.getLogger(ProductDetailPage.class);

    // TODO: Locator para el nombre del producto en la pantalla de detalle
    private static final By PRODUCT_NAME = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para el botón "Add to Cart"
    private static final By ADD_TO_CART_BUTTON = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para el botón de volver atrás
    // Busca el ícono o botón que permite volver a la pantalla anterior
    private static final By BACK_BUTTON = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para el precio del producto
    private static final By PRODUCT_PRICE = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para la descripción del producto
    private static final By PRODUCT_DESCRIPTION = By.xpath("TODO_COMPLETAR");

    /**
     * Constructor de la página de detalle del producto.
     *
     * @param driver Driver de Appium
     */
    public ProductDetailPage(AppiumDriver driver) {
        super(driver);
        logger.info("ProductDetailPage inicializada");
    }

    /**
     * Verifica que la página de detalle del producto está cargada.
     *
     * @return true si el nombre del producto es visible
     */
    public boolean isLoaded() {
        try {
            return isVisible(PRODUCT_NAME);
        } catch (Exception e) {
            logger.debug("ProductDetailPage no está cargada: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return Nombre del producto
     */
    public String getProductName() {
        try {
            String name = text(PRODUCT_NAME);
            logger.info("Nombre del producto obtenido: {}", name);
            return name;
        } catch (Exception e) {
            logger.error("Error al obtener nombre del producto: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener el nombre del producto", e);
        }
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return Precio del producto
     */
    public String getProductPrice() {
        try {
            String price = text(PRODUCT_PRICE);
            logger.info("Precio del producto obtenido: {}", price);
            return price;
        } catch (Exception e) {
            logger.debug("No se pudo obtener el precio: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return Descripción del producto
     */
    public String getProductDescription() {
        try {
            String description = text(PRODUCT_DESCRIPTION);
            logger.info("Descripción del producto obtenida");
            return description;
        } catch (Exception e) {
            logger.debug("No se pudo obtener la descripción: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Añade el producto al carrito.
     *
     * Hace clic en el botón "Añadir al carrito".
     */
    public void addToCart() {
        try {
            tap(ADD_TO_CART_BUTTON);
            logger.info("Producto añadido al carrito");
        } catch (Exception e) {
            logger.error("Error al añadir producto al carrito: {}", e.getMessage());
            throw new RuntimeException("No se pudo añadir el producto al carrito", e);
        }
    }

    /**
     * Vuelve a la página anterior (lista de productos).
     */
    public void goBack() {
        try {
            tap(BACK_BUTTON);
            logger.info("Volviendo a la página anterior");
            pause(500); // Pausa para permitir que la navegación se complete
        } catch (Exception e) {
            logger.error("Error al volver: {}", e.getMessage());
            throw new RuntimeException("No se pudo volver a la página anterior", e);
        }
    }

    /**
     * Realiza la acción completa: añade el producto al carrito y vuelve atrás.
     */
    public void addToCartAndGoBack() {
        try {
            addToCart();
            pause(500); // Espera a que se procese la acción
            goBack();
            logger.info("Producto añadido al carrito y volviendo");
        } catch (Exception e) {
            logger.error("Error en addToCartAndGoBack: {}", e.getMessage());
            throw new RuntimeException("No se pudo completar la acción", e);
        }
    }
}
