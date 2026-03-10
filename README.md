# Proyecto Appium - Ejercicio Práctico para Estudiantes

## Descripción

Este proyecto contiene un ejercicio práctico de automatización mobile con Appium. Los **Page Objects tienen los locators vacíos** (`TODO_COMPLETAR`), y tu tarea es usar **Appium Inspector** para encontrar los locators correctos y completar los archivos.

Esto simula un escenario real donde necesitas investigar una aplicación móvil y encontrar los elementos para automatizarlos.

Para este repo básico, la app se considera iniciando en **ProductsPage**.

## Estructura del Proyecto

```
proyecto_appium_estudiantes/
├── pom.xml                                      # Configuración Maven
├── src/test/
│   ├── java/com/knotacademy/qc/appium/
│   │   ├── core/
│   │   │   ├── ConfigLoader.java               # Cargador de propiedades
│   │   │   ├── DriverFactory.java              # Factory para crear drivers
│   │   │   └── MobileBaseTest.java             # Clase base para tests
│   │   ├── pages/
│   │   │   ├── MobileBasePage.java             # Clase base para Page Objects
│   │   │   ├── LoginPage.java                  # [TODO: Completar locators]
│   │   │   ├── ProductsPage.java               # [TODO: Completar locators]
│   │   │   ├── ProductDetailPage.java          # [TODO: Completar locators]
│   │   │   └── CartPage.java                   # [TODO: Completar locators]
│   │   └── tests/
│   │       └── session/
│   │           ├── LaunchTest.java             # Tests de lanzamiento
│   │           ├── LoginTest.java              # Tests de login
│   │           ├── NavigationTest.java         # Tests de navegación
│   │           ├── FormTest.java               # Tests de formularios
│   │           ├── ScrollTest.java             # Tests de scroll
│   │           └── CartFlowTest.java           # Tests end-to-end
│   └── resources/
│       ├── appium.properties                   # Config general
│       ├── android.properties                  # Config Android
│       └── ios.properties                      # Config iOS
└── README.md                                    # Este archivo
```

## Archivos a Completar

### 1. LoginPage.java
Necesitas encontrar los locators para:
- Campo de usuario (username input)
- Campo de contraseña (password input)
- Botón de login
- Mensaje de error
- Título "Login" (para verificar que la página está cargada)

**Ubicación:** `src/test/java/com/knotacademy/qc/appium/pages/LoginPage.java`

### 2. ProductsPage.java
Necesitas encontrar los locators para:
- Título "Products" (para verificar que la página está cargada)
- Items de productos (contenedores de cada producto en la lista)
- Badge del carrito (ícono/elemento que muestra cantidad de items)

**Ubicación:** `src/test/java/com/knotacademy/qc/appium/pages/ProductsPage.java`

### 3. ProductDetailPage.java
Necesitas encontrar los locators para:
- Nombre del producto
- Botón "Add to Cart" (agregar al carrito)
- Botón de volver atrás
- Precio del producto
- Descripción del producto

**Ubicación:** `src/test/java/com/knotacademy/qc/appium/pages/ProductDetailPage.java`

### 4. CartPage.java
Necesitas encontrar los locators para:
- Título del carrito (para verificar que la página está cargada)
- Items del carrito (contenedores de cada artículo)
- Botón de checkout
- Mensaje de carrito vacío
- Precio total

**Ubicación:** `src/test/java/com/knotacademy/qc/appium/pages/CartPage.java`

## Pasos para Completar el Ejercicio

### Paso 1: Configurar el Appium Inspector

1. Asegúrate de tener Appium Server ejecutándose en `http://127.0.0.1:4723`
2. Verifica que tienes el APK de SauceLabs Demo App en `apps/SauceLabs.apk`
3. Abre Appium Inspector
4. Configura las capabilities según `src/test/resources/android.properties`

### Paso 2: Inspeccionar la Aplicación

1. Inicia Appium Inspector con la app
2. Navega por las pantallas:
   - **Pantalla de Productos:** título, items, carrito badge
   - **Pantalla de Detalle:** nombre, precio, descripción, add to cart, back button
   - **Pantalla de Carrito:** título, items, total, checkout button
   - **Pantalla de Login (opcional):** username, password, login button, error message
     - Flujo para abrir login: **View menu** → **Log In**

3. Para cada elemento, nota el locator preferido en este orden:
   - `content-desc` (accessibility id)
   - `resource-id` (id del recurso)
   - `xpath` (si no hay otras opciones)

### Paso 3: Completar los Page Objects

Abre cada archivo `.java` en la carpeta `pages/` y reemplaza `TODO_COMPLETAR` con los locators reales que encontraste.

**Ejemplo:**
```java
// Antes
private static final By USERNAME_FIELD = By.xpath("TODO_COMPLETAR");

// Después
private static final By USERNAME_FIELD = By.xpath("//*[@content-desc='Username input field']");
// O
private static final By USERNAME_FIELD = By.xpath("//*[@resource-id='com.saucelabs.mydemoapp.android:id/username']");
```

### Paso 4: Ejecutar los Tests

Una vez completados los locators, ejecuta los tests en este orden:

```bash
# Test 1: Verificar que la app se abre (más simple)
mvn clean test -Dtest=LaunchTest

# Test 2: Verificar navegación entre pantallas
mvn clean test -Dtest=NavigationTest

# Test 3: Verificar scroll
mvn clean test -Dtest=ScrollTest

# Test 4: Verificar flujo completo del carrito (end-to-end)
mvn clean test -Dtest=CartFlowTest

# Opcional: pruebas ligadas a LoginPage (pueden quedar omitidas si la app abre en ProductsPage)
mvn clean test -Dtest=LoginTest
mvn clean test -Dtest=FormTest

# Ejecutar todos los tests
mvn clean test
```

### Paso 5: Revisar resultados en Allure

Este proyecto ya tiene integración con Allure en `pom.xml` (`allure-junit5` + `allure-maven`).

1. Ejecuta uno o varios tests:
```bash
# Clase completa
mvn -Dtest=LaunchTest test

# Método puntual
mvn -Dtest='LaunchTest#testAppLaunchesSuccessfully' test

# Suite completa
mvn clean test
```

2. Abre el reporte interactivo:
```bash
mvn allure:serve
```

3. Si prefieres reporte estático (sin servidor temporal):
```bash
mvn allure:report
```
Luego abre:
`target/site/allure-maven-plugin/index.html`

4. Ubicaciones importantes:
- Resultados crudos de ejecución: `target/allure-results/`
- Reporte HTML generado: `target/site/allure-maven-plugin/`
- Resultados de surefire: `target/surefire-reports/`

5. Si quieres evitar mezclar corridas viejas con nuevas:
```bash
mvn clean
```

## Credenciales de Prueba

Use estas credenciales para escenarios de login (opcionales):
- **Usuario:** `alice@example.com`
- **Contraseña:** `10203040`

## Qué revisar en Allure

1. **Overview:** total de pruebas, porcentaje de éxito y duración general.
2. **Suites:** qué clase/método falló exactamente.
3. **Defects:** agrupación de fallos por tipo de error.
4. **Timeline:** tiempos y orden de ejecución.
5. **Behaviors:** agrupación por `@Epic`, `@Feature`, `@Story`.
6. **Stack trace del fallo:** causa real del error (locator, timeout, assertion, etc.).

Tip: cuando falle un test, cruza Allure con `target/surefire-reports` para ver el detalle completo del stacktrace.

## Consejos para Encontrar Locators

### Con Appium Inspector:
1. Toca/clickea el elemento en la pantalla
2. En la sección "Selected Element" verás los atributos
3. Busca `content-desc` primero (es más estable)
4. Si no hay, usa `resource-id`
5. Como último recurso, crea un `xpath`

### Tipos de Locators Comunes en Android:

```java
// Por content-desc (accessibility id)
By.xpath("//*[@content-desc='Login button']")

// Por resource-id
By.xpath("//*[@resource-id='com.saucelabs.mydemoapp.android:id/login']")

// Por texto
By.xpath("//*[contains(text(), 'Login')]")

// Por clase
By.xpath("//android.widget.Button[@text='Login']")

// Combinados
By.xpath("//*[@content-desc='Login button' or @resource-id='com.saucelabs.mydemoapp.android:id/login']")
```

## Estructura de un Page Object Completo

```java
public class MyPage extends MobileBasePage {

    // Localizadores en la parte superior
    private static final By MY_ELEMENT = By.xpath("...locator aqui...");

    // Constructor
    public MyPage(AppiumDriver driver) {
        super(driver);
    }

    // Métodos para interactuar con la página
    public void doSomething() {
        tap(MY_ELEMENT);  // click
        type(MY_ELEMENT, "texto");  // escribir
        text(MY_ELEMENT);  // obtener texto
        isVisible(MY_ELEMENT);  // verificar visibilidad
    }
}
```

## Archivos Base (Referenciales)

Los siguientes archivos base ya están configurados y normalmente no requieren cambios:

- ✓ `ConfigLoader.java` - Cargador de propiedades
- ✓ `DriverFactory.java` - Factory de drivers
- ✓ `MobileBaseTest.java` - Clase base para tests
- ✓ `MobileBasePage.java` - Clase base para pages
- ✓ Archivos de propiedades

Los archivos de tests sí pueden ajustarse según tu práctica de la sesión.

## Flujo de la Aplicación

```
Inicio → ProductsPage → ProductDetailPage
                     ↓
                  CartPage → Checkout

LoginPage queda disponible solo para escenarios puntuales de autenticación.
```

## Recursos Útiles

- [Appium Documentation](https://appium.io/docs/)
- [Appium Inspector Guide](https://github.com/appium/appium-inspector)
- [XPath Tutorial](https://www.w3schools.com/xml/xpath_intro.asp)
- [SauceLabs Demo App Repository](https://github.com/saucelabs/my-demo-app-android)

## Solución Paso a Paso

Si necesitas ayuda, aquí está el orden recomendado:

1. **ProductsPage** → 3 elementos principales
2. **ProductDetailPage** → 5 elementos
3. **CartPage** → 5 elementos
4. **LoginPage (opcional)** → útil para practicar validaciones de autenticación

Una vez completes ProductsPage, ejecuta `LaunchTest` y `NavigationTest` para validar el flujo base.

## Troubleshooting

### Allure no abre o no muestra datos
- Verifica que ejecutaste pruebas antes de abrir Allure.
- Confirma que exista `target/allure-results/`.
- Si el reporte se ve vacío, corre `mvn clean test` y luego `mvn allure:serve`.

### "Elemento no encontrado"
- Verifica el locator en Appium Inspector
- Asegúrate de que la app está en la pantalla correcta
- Intenta usar `content-desc` en lugar de `resource-id`

### "Appium Server no responde"
```bash
# Inicia el servidor en otra terminal
appium --address 127.0.0.1 --port 4723
```

### "APK no encontrado"
```bash
# Asegúrate de que el APK está en:
# apps/SauceLabs.apk
# Relativo a la carpeta donde ejecutas mvn
```

### Los tests no se ejecutan
```bash
# Instala las dependencias
mvn clean install

# Ejecuta un test específico
mvn clean test -Dtest=LaunchTest -v
```

### Warning de script timeout en Android
- Es esperado en algunas combinaciones Appium + UiAutomator2.
- El proyecto ya lo maneja con warning y la ejecución continúa.

## Notas Importantes

1. **Paciencia:** Encontrar locators lleva tiempo. Lee bien los atributos del elemento.
2. **Estabilidad:** Prefiere `content-desc` > `resource-id` > `xpath`
3. **Testing:** Ejecuta los tests después de completar cada página.
4. **Logs:** Los tests generan logs en `target/logs/` que te ayudan a diagnosticar problemas.

## Checklist Antes de Sesión

- Appium levantado en `http://127.0.0.1:4723`.
- Emulador/dispositivo Android encendido y desbloqueado.
- APK disponible en `apps/SauceLabs.apk`.
- Correr smoke rápido: `mvn -Dtest=LaunchTest test`.
- Revisar reporte: `mvn allure:serve`.
- Si vas a correr flujos de carrito/navegación, completar locators pendientes en:
  - `ProductsPage` (`PRODUCT_ITEMS`, `CART_BADGE`)
  - `ProductDetailPage`
  - `CartPage`

## Criterios de Éxito

Tu ejercicio está completo cuando:

- [x] Todos los archivos Page Objects tienen locators reemplazados (sin `TODO_COMPLETAR`)
- [x] `mvn clean test` ejecuta todos los tests sin errores
- [x] Al menos 80% de los tests pasan (algunos pueden ser flaky por timing)
- [x] Los locators son específicos y funcionan repetidamente

## Próximos Pasos (Opcional)

Una vez completes el ejercicio:

1. Mejora los tests agregando más validaciones
2. Crea nuevas páginas para otras pantallas de la app
3. Intenta ejecutar los tests en iOS (cambia `platform=ios` en `appium.properties`)
4. Agrega reportes Allure: `mvn allure:serve`

---

**Creado por:** QA Automation Team
**Versión:** 1.0
**Última actualización:** Marzo 2026
