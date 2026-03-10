package com.knotacademy.qc.appium.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * CartPage: Página del carrito de compras en SauceLabs Demo App.
 *
 * Proporciona métodos para:
 * - Verificar si el carrito está cargado
 * - Obtener cantidad de artículos
 * - Obtener información de los artículos
 * - Proceder al checkout
 *
 * @author QA Automation Team
 * @version 1.0
 */
public class CartPage extends MobileBasePage {

    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    // TODO: Locator para el título del carrito
    // Busca el texto "Cart" o elemento que indique que estamos en la página del carrito
    private static final By CART_TITLE = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para cada item en el carrito
    // Busca los contenedores que representen cada artículo en el carrito
    private static final By CART_ITEMS = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para el botón de checkout
    private static final By CHECKOUT_BUTTON = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para el mensaje de carrito vacío
    private static final By EMPTY_CART_MESSAGE = By.xpath("TODO_COMPLETAR");

    // TODO: Locator para el precio total del carrito
    private static final By CART_TOTAL = By.xpath("TODO_COMPLETAR");

    /**
     * Constructor de la página del carrito.
     *
     * @param driver Driver de Appium
     */
    public CartPage(AppiumDriver driver) {
        super(driver);
        logger.info("CartPage inicializada");
    }

    /**
     * Verifica que la página del carrito está cargada.
     *
     * Busca el título "Cart" en la página.
     *
     * @return true si el título del carrito es visible
     */
    public boolean isLoaded() {
        try {
            return isVisible(CART_TITLE);
        } catch (Exception e) {
            logger.debug("CartPage no está cargada: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el número de artículos en el carrito.
     *
     * @return Cantidad de artículos en el carrito
     */
    public int getItemCount() {
        try {
            List<WebElement> items = driver.findElements(CART_ITEMS);
            int count = items.size();
            logger.info("Artículos en el carrito: {}", count);
            return count;
        } catch (Exception e) {
            logger.debug("Error al contar artículos: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Verifica si el carrito está vacío.
     *
     * @return true si el carrito está vacío
     */
    public boolean isEmpty() {
        try {
            if (isVisible(EMPTY_CART_MESSAGE)) {
                logger.info("El carrito está vacío");
                return true;
            }
        } catch (Exception e) {
            logger.debug("No se encontró mensaje de carrito vacío: {}", e.getMessage());
        }
        return getItemCount() == 0;
    }

    /**
     * Verifica si el carrito tiene artículos.
     *
     * @return true si hay artículos en el carrito
     */
    public boolean hasItems() {
        return getItemCount() > 0;
    }

    /**
     * Obtiene el precio total del carrito.
     *
     * @return Precio total como string
     */
    public String getTotalPrice() {
        try {
            String total = text(CART_TOTAL);
            logger.info("Precio total obtenido: {}", total);
            return total;
        } catch (Exception e) {
            logger.debug("No se pudo obtener el precio total: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Procede al checkout.
     *
     * Hace clic en el botón de checkout.
     */
    public void checkout() {
        try {
            tap(CHECKOUT_BUTTON);
            logger.info("Procediendo al checkout");
        } catch (Exception e) {
            logger.error("Error al proceder al checkout: {}", e.getMessage());
            throw new RuntimeException("No se pudo proceder al checkout", e);
        }
    }

    /**
     * Obtiene el texto del artículo en una posición específica.
     *
     * @param index Índice del artículo (basado en 0)
     * @return Texto del artículo
     */
    public String getItemText(int index) {
        try {
            List<WebElement> items = driver.findElements(CART_ITEMS);
            if (index < 0 || index >= items.size()) {
                throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
            }

            String itemText = items.get(index).getText();
            logger.info("Texto del artículo en índice {}: {}", index, itemText);
            return itemText;
        } catch (Exception e) {
            logger.error("Error al obtener texto del artículo: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener el texto del artículo", e);
        }
    }
}
