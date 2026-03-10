package com.knotacademy.qc.appium.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;

/**
 * MobileBasePage: Clase base para todas las páginas de Appium.
 *
 * Proporciona métodos comunes para interactuar con elementos en aplicaciones móviles:
 * - Tap/Click
 * - Text input
 * - Scroll y swipe
 * - Visibilidad de elementos
 * - Manejo del teclado
 *
 * Todos los métodos incluyen logging y manejo de excepciones robusto.
 *
 * @author QA Automation Team
 * @version 1.0
 */
public abstract class MobileBasePage {

    private static final Logger logger = LoggerFactory.getLogger(MobileBasePage.class);

    protected AppiumDriver driver;
    protected WebDriverWait wait;

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Constructor de la página móvil.
     *
     * @param driver Driver de Appium
     */
    public MobileBasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        logger.info("Página {} inicializada", this.getClass().getSimpleName());
    }

    /**
     * Realiza un tap/click en un elemento.
     *
     * @param by Localizador del elemento
     */
    public void tap(By by) {
        try {
            WebElement element = waitForElement(by);
            element.click();
            logger.info("Tap realizado en elemento: {}", by);
        } catch (Exception e) {
            logger.error("Error al hacer tap en elemento {}: {}", by, e.getMessage());
            throw new RuntimeException("No se pudo hacer tap en: " + by, e);
        }
    }

    /**
     * Escribe texto en un campo de entrada.
     *
     * Limpia el campo antes de escribir.
     *
     * @param by Localizador del elemento
     * @param text Texto a escribir
     */
    public void type(By by, String text) {
        try {
            WebElement element = waitForElement(by);
            element.clear();
            element.sendKeys(text);
            logger.info("Texto escrito en elemento {}: '{}'", by, text);
        } catch (Exception e) {
            logger.error("Error al escribir en elemento {}: {}", by, e.getMessage());
            throw new RuntimeException("No se pudo escribir en: " + by, e);
        }
    }

    /**
     * Obtiene el texto de un elemento.
     *
     * @param by Localizador del elemento
     * @return Texto del elemento
     */
    public String text(By by) {
        try {
            WebElement element = waitForElement(by);
            String text = element.getText();
            logger.info("Texto obtenido de elemento {}: '{}'", by, text);
            return text;
        } catch (Exception e) {
            logger.error("Error al obtener texto de elemento {}: {}", by, e.getMessage());
            throw new RuntimeException("No se pudo obtener texto de: " + by, e);
        }
    }

    /**
     * Verifica si un elemento es visible.
     *
     * @param by Localizador del elemento
     * @return true si el elemento es visible, false en caso contrario
     */
    public boolean isVisible(By by) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            boolean visible = element.isDisplayed();
            logger.info("Elemento {} visible: {}", by, visible);
            return visible;
        } catch (Exception e) {
            logger.debug("Elemento {} no es visible: {}", by, e.getMessage());
            return false;
        }
    }

    /**
     * Espera a que un elemento esté presente y visible.
     *
     * @param by Localizador del elemento
     * @return El elemento encontrado
     * @throws Exception Si el elemento no se encuentra en el tiempo de espera
     */
    protected WebElement waitForElement(By by) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Timeout esperando elemento: {}", by);
            throw new RuntimeException("Elemento no encontrado: " + by, e);
        }
    }

    /**
     * Desplaza (scroll) hasta encontrar un elemento por su texto.
     *
     * Para Android, usa UiScrollable.
     * Para iOS, usa scroll con Actions.
     *
     * @param text Texto del elemento a buscar
     */
    public void scrollToElement(String text) {
        try {
            if (isAndroid()) {
                scrollToElementAndroid(text);
            } else {
                scrollToElementIOS(text);
            }
            logger.info("Scroll realizado para encontrar: {}", text);
        } catch (Exception e) {
            logger.error("Error al hacer scroll para encontrar '{}': {}", text, e.getMessage());
            throw new RuntimeException("No se pudo hacer scroll a: " + text, e);
        }
    }

    /**
     * Scroll en Android usando UiScrollable.
     *
     * @param text Texto del elemento a buscar
     */
    private void scrollToElementAndroid(String text) {
        String scrollCommand = "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().text(\"" + text + "\"))";
        driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(scrollCommand));
    }

    /**
     * Scroll en iOS usando Actions.
     *
     * @param text Texto del elemento a buscar
     */
    private void scrollToElementIOS(String text) {
        // En iOS, se usa scroll más tradicional con Actions
        WebElement element = driver.findElement(By.xpath("//*[contains(text(), '" + text + "')]"));
        Actions actions = new Actions(driver);
        actions.scrollToElement(element).perform();
    }

    /**
     * Realiza un swipe en una dirección específica.
     *
     * Utiliza W3C Actions que funcionan en Android e iOS.
     *
     * @param direction Dirección del swipe: UP, DOWN, LEFT, RIGHT
     */
    public void swipe(String direction) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe;

            // Obtener dimensiones de la pantalla
            int width = driver.manage().window().getSize().getWidth();
            int height = driver.manage().window().getSize().getHeight();

            switch (direction.toUpperCase()) {
                case "UP" -> {
                    // Swipe from bottom to top
                    swipe = new Sequence(finger, 1);
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
                            width / 2, (int) (height * 0.8)));
                    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    swipe.addAction(new Pause(finger, Duration.ofMillis(200)));
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(),
                            width / 2, (int) (height * 0.2)));
                    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }
                case "DOWN" -> {
                    // Swipe from top to bottom
                    swipe = new Sequence(finger, 1);
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
                            width / 2, (int) (height * 0.2)));
                    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    swipe.addAction(new Pause(finger, Duration.ofMillis(200)));
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(),
                            width / 2, (int) (height * 0.8)));
                    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }
                case "LEFT" -> {
                    // Swipe from right to left
                    swipe = new Sequence(finger, 1);
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
                            (int) (width * 0.8), height / 2));
                    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    swipe.addAction(new Pause(finger, Duration.ofMillis(200)));
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(),
                            (int) (width * 0.2), height / 2));
                    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }
                case "RIGHT" -> {
                    // Swipe from left to right
                    swipe = new Sequence(finger, 1);
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(),
                            (int) (width * 0.2), height / 2));
                    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    swipe.addAction(new Pause(finger, Duration.ofMillis(200)));
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(),
                            (int) (width * 0.8), height / 2));
                    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }
                default -> throw new IllegalArgumentException("Dirección no válida: " + direction);
            }

            driver.perform(Arrays.asList(swipe));
            logger.info("Swipe realizado en dirección: {}", direction);
        } catch (Exception e) {
            logger.error("Error al hacer swipe en dirección {}: {}", direction, e.getMessage());
            throw new RuntimeException("No se pudo hacer swipe: " + direction, e);
        }
    }

    /**
     * Oculta el teclado en la pantalla.
     *
     * Método específico para cada plataforma.
     */
    public void hideKeyboard() {
        try {
            if (isAndroid()) {
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            } else {
                // Para iOS, hacer tap fuera del campo activo
                int width = driver.manage().window().getSize().getWidth();
                int height = driver.manage().window().getSize().getHeight();
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tapOutside = new Sequence(finger, 1);
                tapOutside.addAction(finger.createPointerMove(Duration.ofMillis(0),
                        PointerInput.Origin.viewport(), width / 2, 10));
                tapOutside.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tapOutside.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(Arrays.asList(tapOutside));
            }
            logger.info("Teclado oculto");
        } catch (Exception e) {
            logger.warn("No se pudo ocultar el teclado: {}", e.getMessage());
        }
    }

    /**
     * Verifica si la plataforma actual es Android.
     *
     * @return true si es Android, false en caso contrario
     */
    protected boolean isAndroid() {
        return driver instanceof AndroidDriver;
    }

    /**
     * Verifica si la plataforma actual es iOS.
     *
     * @return true si es iOS, false en caso contrario
     */
    protected boolean isIOS() {
        return driver instanceof IOSDriver;
    }

    /**
     * Pausa la ejecución por un tiempo determinado.
     *
     * @param milliseconds Milisegundos a esperar
     */
    protected void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Pausa interrumpida: {}", e.getMessage());
        }
    }
}
