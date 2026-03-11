package com.knotacademy.qc.appium.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginPage: Página de login de la aplicación SauceLabs Demo App.
 *
 * Proporciona métodos para:
 * - Ingresar usuario y contraseña
 * - Hacer clic en el botón de login
 * - Verificar mensajes de error
 *
 * Utiliza localizadores basados en Accessibility IDs cuando es posible.
 *
 * @author QA Automation Team
 * @version 1.0
 */
public class LoginPage extends MobileBasePage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // TODO: Usar Appium Inspector para encontrar el locator del campo de usuario
    // Pista: Busca el content-desc o resource-id del campo de texto del username
    private static final By USERNAME_FIELD = By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]");

    // TODO: Usar Appium Inspector para encontrar el locator del campo de contraseña
    private static final By PASSWORD_FIELD = By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/passwordET\"]");

    // TODO: Usar Appium Inspector para encontrar el locator del botón de login
    private static final By LOGIN_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Tap to login with given credentials\"]");

    // TODO: Buscar el elemento que muestra el mensaje de error
    private static final By ERROR_MESSAGE = By.xpath(
        "//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameErrorTV\"]");

    private static final By ERROR_LOCK = By.xpath(
        "//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/passwordErrorTV\"]");

    // TODO: Locator para verificar que estamos en la pantalla de login (busca el título "Login")
    private static final By LOGIN_TITLE = By.xpath("//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/loginTV\"]");

    /**
     * Constructor de la página de login.
     *
     * @param driver Driver de Appium
     */
    public LoginPage(AppiumDriver driver) {
        super(driver);
        logger.info("LoginPage inicializada");
    }

    /**
     * Verifica que la página de login está visible.
     *
     * @return true si el título "Login" es visible
     */
    public boolean isLoaded() {
        try {
            return isVisible(LOGIN_TITLE);
        } catch (Exception e) {
            logger.debug("LoginPage no está cargada: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Realiza el login con usuario y contraseña.
     *
     * Pasos:
     * 1. Ingresa el usuario en el campo de usuario
     * 2. Ingresa la contraseña en el campo de contraseña
     * 3. Oculta el teclado
     * 4. Hace clic en el botón de login
     *
     * @param username Usuario para el login
     * @param password Contraseña para el login
     */
    public void login(String username, String password) {
        logger.info("Iniciando login con usuario: {}", username);

        try {
            type(USERNAME_FIELD, username);
            type(PASSWORD_FIELD, password);
            try {
                tap(LOGIN_BUTTON);
            } catch (RuntimeException firstTapError) {
                logger.debug("Primer intento de tap en login falló, reintentando tras ocultar teclado: {}",
                        firstTapError.getMessage());
                hideKeyboard();
                tap(LOGIN_BUTTON);
            }

            logger.info("Login completado");
        } catch (Exception e) {
            logger.error("Error durante el login: {}", e.getMessage());
            throw new RuntimeException("No se pudo completar el login", e);
        }
    }

    /**
     * Obtiene el mensaje de error de la pantalla de login.
     *
     * Útil para validar intentos fallidos de login.
     *
     * @return Mensaje de error, o cadena vacía si no hay error
     */
    public String getErrorMessage() {
        try {
            if (isVisible(ERROR_MESSAGE)) {
                String errorMessage = text(ERROR_MESSAGE);
                logger.info("Mensaje de error obtenido: {}", errorMessage);
                return errorMessage;
            }
        } catch (Exception e) {
            logger.debug("No hay mensaje de error visible: {}", e.getMessage());
        }
        return "";
    }

    public String getErrorLock() {
        try {
            if (isVisible(ERROR_LOCK)) {
                String errorMessage = text(ERROR_LOCK);
                logger.info("Mensaje de error obtenido: {}", errorMessage);
                return errorMessage;
            }
        } catch (Exception e) {
            logger.debug("No hay mensaje de error visible: {}", e.getMessage());
        }
        return "";
    }

    /**
     * Limpia el campo de usuario.
     */
    public void clearUsername() {
        try {
            driver.findElement(USERNAME_FIELD).clear();
            logger.info("Campo de usuario limpiado");
        } catch (Exception e) {
            logger.warn("No se pudo limpiar el campo de usuario: {}", e.getMessage());
        }
    }

    /**
     * Limpia el campo de contraseña.
     */
    public void clearPassword() {
        try {
            driver.findElement(PASSWORD_FIELD).clear();
            logger.info("Campo de contraseña limpiado");
        } catch (Exception e) {
            logger.warn("No se pudo limpiar el campo de contraseña: {}", e.getMessage());
        }
    }
}
