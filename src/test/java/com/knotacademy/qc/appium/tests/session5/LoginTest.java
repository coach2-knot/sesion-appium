package com.knotacademy.qc.appium.tests.session5;

import com.knotacademy.qc.appium.core.MobileBaseTest;
import com.knotacademy.qc.appium.pages.LoginPage;
import com.knotacademy.qc.appium.pages.ProductsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginTest: Pruebas del flujo de login (Session 5).
 *
 * Verifica que:
 * 1. El login válido funciona correctamente
 * 2. El login inválido muestra un mensaje de error
 * 3. El formulario de login responde correctamente a los datos
 *
 * @author QA Automation Team
 * @version 1.0
 */
@Epic("Session 5 - Mobile App Fundamentals")
@Feature("Authentication")
public class LoginTest extends MobileBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    private LoginPage requireLoginPageForTest() {
        Assumptions.assumeTrue(isLoginPageVisible(),
                "Se omite: esta ejecución inicia en ProductsPage y no expone LoginPage al arranque.");
        return new LoginPage(driver);
    }

    @Test
    @DisplayName("S5-101: Login válido debe permitir acceso a la aplicación")
    @Story("Valid Login")
    @Description("Verifica que el login con credenciales válidas permite acceder a la pantalla de productos")
    public void testValidLogin() {
        logger.info("=== Iniciando test: testValidLogin ===");

        // Verificar precondición de login
        LoginPage loginPage = requireLoginPageForTest();
        logger.info("Pantalla de login verificada");

        // Realizar login con credenciales válidas
        String validUsername = "alice@example.com";
        String validPassword = "10203040";
        loginPage.login(validUsername, validPassword);
        logger.info("Login realizado con usuario: {}", validUsername);

        // Esperar a que cargue la página de productos
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(5));

        // Verificar que se accedió a la página de productos
        ProductsPage productsPage = new ProductsPage(driver);
        assertTrue(productsPage.isLoaded(), "Después del login válido, debe mostrarse la pantalla de productos");

        logger.info("✓ Test completado: Login válido exitoso");
    }

    @Test
    @DisplayName("S5-102: Login inválido debe mostrar error")
    @Story("Invalid Login")
    @Description("Verifica que el login con credenciales inválidas muestra un mensaje de error")
    public void testInvalidLogin() {
        logger.info("=== Iniciando test: testInvalidLogin ===");

        // Página de login
        LoginPage loginPage = requireLoginPageForTest();

        // Realizar login con credenciales inválidas
        String invalidUsername = "invalid@example.com";
        String invalidPassword = "wrongpassword";
        loginPage.login(invalidUsername, invalidPassword);
        logger.info("Login realizado con credenciales inválidas");

        // Esperar a que aparezca el mensaje de error
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(3));

        // Verificar que se muestra el mensaje de error
        String errorMessage = loginPage.getErrorMessage();
        assertFalse(errorMessage.isEmpty(), "Debe mostrarse un mensaje de error para credenciales inválidas");
        logger.info("Mensaje de error obtenido: {}", errorMessage);

        logger.info("✓ Test completado: Error de login mostrado correctamente");
    }

    @Test
    @DisplayName("S5-103: Campos vacíos deben requerir datos")
    @Story("Form Validation")
    @Description("Verifica que intentar login sin datos muestra un error")
    public void testEmptyFieldsLogin() {
        logger.info("=== Iniciando test: testEmptyFieldsLogin ===");

        LoginPage loginPage = requireLoginPageForTest();

        // Intentar login sin credenciales (campos vacíos)
        loginPage.login("", "");
        logger.info("Intento de login con campos vacíos");

        // Esperar validación
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(3));

        // Verificar que se muestra error
        String errorMessage = loginPage.getErrorMessage();
        assertFalse(errorMessage.isEmpty(), "Debe mostrarse error para campos vacíos");
        logger.info("Error de validación obtenido: {}", errorMessage);

        logger.info("✓ Test completado: Validación de campos vacíos funcionando");
    }

    @Test
    @DisplayName("S5-104: Login con usuario válido y contraseña inválida")
    @Story("Invalid Login")
    @Description("Verifica el comportamiento con datos parcialmente correctos")
    public void testValidUsernameInvalidPassword() {
        logger.info("=== Iniciando test: testValidUsernameInvalidPassword ===");

        LoginPage loginPage = requireLoginPageForTest();

        // Login con usuario válido pero contraseña incorrecta
        loginPage.login("alice@example.com", "wrongpassword");
        logger.info("Login con usuario válido y contraseña inválida");

        // Esperar validación
        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(3));

        // Verificar error
        String errorMessage = loginPage.getErrorMessage();
        assertFalse(errorMessage.isEmpty(), "Debe mostrarse error para contraseña incorrecta");
        logger.info("Error obtenido: {}", errorMessage);

        logger.info("✓ Test completado: Validación de credenciales parciales funcionando");
    }
}
