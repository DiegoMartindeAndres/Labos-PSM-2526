# 🍽️ Continuación aplicación de recetas: Navegación a los detalles de categoría.

En este laboratorio vamos a continuar con la aplicación de recetas y para ello vamos a mejorarla creando una pantalla de detalle para las diferentes categorías de nuestra aplicación de recetas. La idea es que, una vez que hagamos clic en una de las categorías, podamos navegar a otra pantalla donde se muestre el detalle de dicha categoría. Esta pantalla incluirá un título, una imagen y un texto que se pueda desplazar verticalmente. Según podemos ver en la siguiente imagen:

<div align="center">
    <img src="img/app.gif" alt="Ejemplo app.">
</div>

# 📋 Tabla de Contenidos 
- [🍽️ Continuación aplicación de recetas: Navegación a los detalles de categoría.](#️-continuación-aplicación-de-recetas-navegación-a-los-detalles-de-categoría)
- [📋 Tabla de Contenidos](#-tabla-de-contenidos)
- [Navegación entre pantallas en la App de Recetas 🛠️](#navegación-entre-pantallas-en-la-app-de-recetas-️)
- [✨ La importancia de la **serialización**](#-la-importancia-de-la-serialización)

### 🚀 Creando la pantalla de detalle

Primero, creamos un nuevo archivo Kotlin dentro de nuestra aplicación de recetas. Este archivo se llamará `CategoryCategoryDetailScreen.kt`. El contenido del archivo será un composable llamado `CategoryCategoryDetailScreen` que recibirá como parámetro una categoría, ya que necesitamos saber qué categoría debemos mostrar.

```kotlin
@Composable
fun CategoryCategoryDetailScreen(category: Category) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = category.strCategory,
            textAlign = TextAlign.Center
        )

        Image(
            painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
            contentDescription = "${category.strCategory} Thumbnail",
            modifier = Modifier
                .wrapContentSize()
                .aspectRatio(1f)
        )

        Text(
            text = category.strCategoryDescription,
            textAlign = TextAlign.Justify,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}
```

### 📝 Explicación del código

1. **Columnas y Modificadores**: Utilizamos un `Column` para organizar el contenido de la pantalla verticalmente. Aplicamos el modificador `fillMaxSize()` para ocupar todo el espacio disponible y `padding(16.dp)` para mantener un espacio alrededor de los elementos y evitar que queden pegados a los bordes de la pantalla. El contenido se alinea horizontalmente en el centro mediante `horizontalAlignment = Alignment.CenterHorizontally`.

2. **Título de la Categoría**: Mostramos el título de la categoría utilizando un componente `Text`. Aplicamos `textAlign = TextAlign.Center` para que el texto se alinee en el centro de la pantalla, logrando un enfoque visual que lo hace destacar.

3. **Imagen de la Categoría**: Para mostrar la imagen de la categoría, usamos el componente `Image` junto con `rememberAsyncImagePainter(model = category.strCategoryThumb)`, que nos permite cargar la imagen desde una URL. La propiedad `contentDescription` se usa para proporcionar una descripción de la imagen, facilitando la accesibilidad, en este caso, indicando que es la miniatura (“Thumbnail”) de la categoría. El modificador `wrapContentSize()` se utiliza para ajustar el tamaño de la imagen a su contenido y `aspectRatio(1f)` asegura que la imagen mantenga una proporción de aspecto cuadrada.

4. **Descripción Desplazable**: Utilizamos un componente `Text` para mostrar la descripción de la categoría, aplicando `textAlign = TextAlign.Justify` para que las líneas queden alineadas de forma uniforme, mejorando la legibilidad. Además, para permitir que el usuario pueda leer la descripción completa si es demasiado larga, agregamos el modificador `verticalScroll(rememberScrollState())`, lo cual hace que el texto sea desplazable verticalmente.

### 🖼️ Comportamiento de Desplazamiento

El uso de `verticalScroll` con `rememberScrollState()` permite que el área del texto sea desplazable, sin desplazar el resto de la pantalla. Esto significa que tanto el título como la imagen permanecerán fijos en su posición mientras el usuario puede deslizarse por la descripción.

# Navegación entre pantallas en la App de Recetas 🛠️

Vamos a trabajar en la implementación de la navegación entre distintas pantallas, teníamos una rejilla de categorías, cada una de ellas compuestas por `CategoryItem`. Vamos a preparar la aplicación para que cuando hagamos clic en una categoría, podamos navegar a la pantalla de detalle de esa categoría en la GUI que acabamos de crear.

### 1. Modificar la cabecera de la función `CategoryItem` 📦

Además de el propio objeto de tipo `Category`, vamos a declarar una función lambda para la navegación llamada `navigateToDetail`. Esta función se encargará de navegar a la pantalla de detalle de la categoría correspondiente. Y recibirá un objeto de tipo `Category` como parámetro.

Para que la pantalla de detalle sepa qué mostrar, necesitamos pasarle información desde la pantalla de recetas. Esta información será la **categoría** seleccionada por el usuario. En lugar de pasar simplemente un identificador o un dato sencillo, como un entero o una cadena, vamos a pasar el objeto completo de la categoría.

Esto nos permite mantener toda la información relevante y facilita el desarrollo, especialmente si la estructura del objeto crece con el tiempo. Para pasar objetos de una pantalla a otra, estos deben ser *serializables*, lo cual significa que deben poder ser convertidos a un formato que permita su transmisión entre componentes.

```kotlin
fun CategoryItem(category: Category,
                 navigateToDetail: (Category) -> Unit) {

// Resto del código
}
```

### 2. Haciendo clic en un elemento de la lista 👉

Cada **CategoryItem** necesita permitir la navegación hacia la pantalla de detalle. Para lograr esto, vamos a utilizar un "**viejo truco**" que tienen todos los elementos `@Composable` de Jetpack Compose, y es que todos los elementos son clicables. Por lo tanto, podemos llamar la función `clickable` de cualquier elemento y definir la acción a tomar cuando el usuario haga clic en dicho elemento.

En nuestro código, vamos a llamar la función `clickable` de toda el área del `Column` que representa un elemento de categoría. Al hacer clic en este elemento, se invocará la función `navigateToDetail` con el objeto de categoría correspondiente como parámetro, permitiendo la navegación a la pantalla de detalle.

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .clickable { navigateToDetail(category) },
    horizontalAlignment = Alignment.CenterHorizontally
) {
    // resto del código.
}
```


### 4. Pasando la responsabilidad 💪

Te habrás dado cuenta que ahora `CategoryScreen` salta un error porque, con los cambios que hemos hecho, se debe pasar una función lambda sobre que hacer cuando se navega a la pantalla de detalle. Pero en realidad tampoco es la responsabilidad de `CategoryScreen` saber cómo navegar a la pantalla de detalle. 

Vamos a pensar: Si tratamos de recordar las llamadas de las funciones de la GUI eran:

`CategoriesGUI` -> `CategoryScreen` -> `CategoryItem`

Donde:
 
- `CategoriesGUI`: Mostraba un elemento `CircularProgressIndicator` mientras se cargaba la información. Y cuando estaba cargado llamaba a `CategoryScreen`
- `CategoryScreen`: Mostraba una `LazyVerticalGrid` llamando a `CategoryItem` con cada categoría.
- `CategoryItem`: Muestra la información concreta de una categoría.

La responsabilidad de la navegación no debe recaer en el componente más bajo, como el **CategoryItem**, sino que debe ser pasada hacia niveles superiores para mejorar la modularidad del código. De esta manera, quien tiene conocimiento sobre la navegación es un nivel superior que controla toda la lógica de la UI.

Así, la responsabilidad de navegar desde el **CategoryItem** se delega al componente **CategoryScreen**, y este a su vez la delega al nivel superior, que es **CategoriesGUI**. 

Por lo tanto, todas estas funciones recibirán una función lambda que se encargará de la navegación y se la irán pasando entre ellas.

Por lo tanto, la nueva cabecera de todas las funciones serán:

```kotlin

@Composable
fun CategoriesGUI(modifier: Modifier = Modifier,
                  navigateToDetail: (Category) -> Unit ){
    // Resto del código
}

@Composable
fun CategoryScreen(categories: List<Category>,
                   navigateToDetail: (Category) -> Unit) {
// Resto del código
}

@Composable
fun CategoryItem(category: Category,
                 navigateToDetail: (Category) -> Unit) {
// Resto del código
}
```

Ahora vamos a realizar las llamadas a las funciones y pasamos la función lambda de navegación entre ellas. OJO!! es muy importante o tu código no funcionará y no voy a poner el código.

Pero... ¿Quién tiene la responsabilidad de pasar la función lambda de navegación a `CategoriesGUI`? 
- Es una buena pregunta, eso lo veremos más adelante. De momento esto generará un error desde donde se llame a `CategoriesGUI`


### 📦 Agregar la dependencia de navegación

Primero, debemos agregar la dependencia necesaria para la navegación en nuestro archivo Gradle. Ya sabes como agregar dependencias, no se hacen directamente en el archivo `build.gradle` de la aplicación. Si no a través de la estructura de proyecto `Project Structure` en Android Studio (Ctrl + Alt + Shift + S).

La dependencia que tenemos que importar debe ser la siguiente. Aunque es la versión usada a la hora de la redacción de este manual.

```kotlin
androidx.navigation:navigation-compose:2.8.4
```

Una vez agregada, sincroniza tu archivo Gradle para que se puedan utilizar las funcionalidades de navegación si no lo hace Android Studio automáticamente.

### 🖥️ Clase `Screen` para manejarlas a todas 💍

Vamos a configurar la clase que se encargará de gestionar las diferentes pantallas de nuestra aplicación, nos hará la vida más fácil. Por el momento, solo tenemos dos pantallas: `CategoriesGUI` y `CategoryDetailScreen`. Sin embargo, en el futuro podríamos extender la aplicación agregando más pantallas, por lo que es buena práctica tenerlas todas organizadas en un solo lugar.

Para ello, crearemos una nueva clase en Kotlin llamada `Screen`.

```kotlin
sealed class Screen(val route: String) {
    data object CategoriesGUI : Screen("recipe_screen")
    data object CategoryDetailScreen : Screen("detail_screen")
}
```

- **Clase sellada (`sealed class`)**: Utilizamos `sealed class` porque sabemos de antemano que la clase `Screen` solo tendrá ciertos subtipos (las diferentes pantallas de la aplicación). Esto nos permite garantizar la seguridad de tipos en tiempo de compilación, evitando errores que podrían surgir al pasar rutas incorrectas.
- **Objetos para cada pantalla**: Creamos un objeto para cada pantalla (—`CategoriesGUI` y `CategoryDetailScreen`—) que extiende de `Screen`. De esta forma, definimos rutas constantes que podremos reutilizar a lo largo del código.

Este enfoque nos ayuda a evitar errores comunes, como escribir mal el nombre de una ruta cuando queremos navegar entre pantallas. Al tener las rutas definidas de esta manera, el IDE nos ayudará a encontrar posibles errores antes de ejecutar la aplicación.

### ¿Qué es una `sealed class`?
En Kotlin, una "sealed class" (clase sellada) es una clase que se utiliza para representar una jerarquía de clases que tiene un número limitado de subtipos conocidos. Es útil cuando deseas modelar un conjunto finito de posibles tipos de datos, proporcionando seguridad al no permitir que otros tipos extiendan la clase más allá de los definidos en el código.

Una sealed class es similar a una clase abstracta en la que puede haber subclases, pero la diferencia clave es que solo puedes tener subclases definidas en el mismo archivo de la clase sellada. Esto facilita el manejo de las variantes posibles, particularmente cuando se trabaja con declaraciones `when`, ya que el compilador puede verificar que todos los casos posibles estén cubiertos.


### 🚀 Ventajas del uso de clases selladas

El uso de `sealed class` nos asegura que todas las rutas están claramente definidas y disponibles en tiempo de compilación. Esto implica que:

- Podemos evitar errores tontos al escribir manualmente las rutas.
- La navegación es más segura, ya que estamos restringidos a utilizar solo las rutas definidas en la clase `Screen`.

En resumen, tener todas las pantallas centralizadas y accesibles a través de una clase sellada facilita el mantenimiento del código y nos asegura que las rutas serán siempre correctas.


### 🚀 Configuración del App Principal

Primero, creamos un archivo llamado `RecipeApp.kt`. Este archivo contiene el composable `RecipeApp`, que se encargará de gestionar la navegación de la aplicación:

```kotlin
@Composable
fun RecipeApp(navController: NavHostController) {
    val recipeViewModel: CategoriesViewModel  = viewModel()
    val viewState by recipeViewModel.categoriesState

    NavHost(
        navController = navController,
        // Aquí debajo hacemos uso de la clase sellada Screen, para evitar errores
        startDestination = Screen.CategoriesGUI.route
    ) {
        // Aquí también hacemos uso de la clase sellada Screen
        composable(route = Screen.CategoriesGUI.route) {
            CategoriesGUI(
                // OJO!! aqui pasamos a RecipeScreen la viewState, y esto no lo recibe como parámetro.
                // Te saldrá como error. Tendremos que cambiarlo en CategoriesGUI.
                viewState = viewState,
                navigateToDetail = { category ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("cat", category)
                    // Volvemos a usar la clase sellada Screen
                    navController.navigate(Screen.CategoryDetailScreen.route)
                }
            )
        }
        composable(route = Screen.CategoryDetailScreen.route) { backStackEntry ->
            val category = navController.previousBackStackEntry?.savedStateHandle?.
            get<Category>("cat") ?: Category("", "", "", "")
            CategoryDetailScreen(category = category)
        }
    }
}
```

Como has visto en el código anterior a `CategoriesGUI` le estamos pasando un nuevo parámetro llamado `viewState` que no estaba definido en el composable. Por lo tanto, debemos modificar la cabecera de `CategoriesGUI` para que reciba este nuevo parámetro, y eliminar la variable `viewState` que teníamos definida en el composable.

```kotlin
@Composable
fun CategoriesGUI(modifier: Modifier = Modifier,
                  viewState : CategoriesViewModel.CategoriesFetchState,
                  navigateToDetail: (Category) -> Unit ){
    val recipeViewModel: CategoriesViewModel = viewModel()
    
    // como hemos pasado el viewState a CategoriesGUI, ya no necesitamos esta variable
    // val viewState by recipeViewModel.categoriesState

    Box(modifier = Modifier.fillMaxSize()){
        when{
            viewState.loading ->{
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }

            viewState.error != null ->{
                Text("Ocurrió un error: $viewState.error")
            }
            else ->{
                CategoryScreen(categories = viewState.categories,
                    navigateToDetail)
            }
        }
    }
}
```

#### 📝 Explicación Detallada del código

Te habrás fijado que hay un par de líneas que necesitan ser explicadas. Estas líneas son fundamentales para gestionar el estado y la navegación entre pantallas utilizando `NavController` en Jetpack Compose.

#### Almacenar la Categoría Seleccionada

```kotlin
navController.currentBackStackEntry?.savedStateHandle?.set("cat", category)
```

### Explicación

- **`navController.currentBackStackEntry`**: Obtiene la entrada actual en la pila de navegación, es decir, la pantalla que está activa en ese momento.
- **`?.savedStateHandle`**: `savedStateHandle` es un contenedor que permite almacenar y gestionar datos entre diferentes pantallas. Al acceder al `savedStateHandle` de la entrada actual (`currentBackStackEntry`), podemos guardar información que se mantendrá disponible incluso al navegar a otras pantallas.
- **`.set("cat", category)`**: Utilizamos el método `set` para almacenar un valor en el `savedStateHandle`. En este caso, estamos almacenando el objeto `category` con la clave `"cat"`. Esto nos permitirá recuperar este valor en la siguiente pantalla. Esto funciona como un diccionario, de hecho, es lo que es.

Esta línea de código se utiliza para guardar la categoría seleccionada por el usuario antes de navegar a la pantalla de detalles. Al guardar la categoría en el `savedStateHandle`, garantizamos que esta información esté disponible para su recuperación cuando la pantalla de detalle necesite mostrar la información correspondiente.

#### Recuperar la Categoría en la Pantalla de Detalle

```kotlin
val category = navController.previousBackStackEntry?.savedStateHandle?.get<Category>("cat") ?: Category("", "", "", "")
```

### Explicación

- **`navController.previousBackStackEntry`**: Obtiene la entrada previa en la pila de navegación, es decir, la pantalla desde la cual se ha navegado a la pantalla actual. En este caso, se refiere a la pantalla que contenía la lista de categorías.
- **`?.savedStateHandle?.get<Category>("cat")`**: Utilizamos el método `get` para recuperar el valor almacenado en el `savedStateHandle` de la entrada previa. Estamos buscando el valor con la clave `"cat"` y especificamos que el tipo de dato es `Category`.
- **`?: Category("", "", "", "")`**: En caso de que no se pueda recuperar la categoría (por ejemplo, si es `null`), se devuelve una instancia vacía de `Category` como valor predeterminado. Esto evita errores y asegura que la aplicación pueda seguir funcionando incluso si no se encuentra el dato esperado.

Esta línea de código es fundamental para obtener la categoría seleccionada previamente y mostrar sus detalles en la pantalla correspondiente. Al utilizar el `savedStateHandle`, nos aseguramos de que la información esté disponible, incluso si el sistema elimina temporalmente la actividad para liberar recursos.

### 🚀 Llamar desde `MainActivity` a `RecipeApp`

Finalmente, en nuestro archivo `MainActivity.kt`, llamamos a `RecipeApp`.


```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            RecetasAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        RecipeApp(navController = navController)
                    }
                }
            }
        }
    }
}
```

Si te fijas lo primero que hacemos después del `setContent` es crear un `NavController` con `rememberNavController()`. Este `NavController` es el encargado de gestionar la navegación entre las diferentes pantallas de nuestra aplicación. Luego, pasamos este `NavController` a `RecipeApp` para que pueda ser utilizado en la navegación.

Para poder evitar el problema que tenemos siempre con el innerPadding, hemos añadido un `Box` que nos permite gestionar el padding de la pantalla de una forma más sencilla y que ocupará toda el `Scaffold`.

### 🥧 Configuración de Serialization y Parcelables

Para pasar objetos complejos entre pantallas, necesitamos hacer que nuestras clases sean "parcelables", que es una nueva forma de decir que son "serializables". Esto se logra añadiendo el plugin `kotlin-parcelize` en nuestro archivo `build.gradle.kts (Module :app)` buscaremos una sección llamada `plugins` y añadiremos el siguiente código. OJO!! solo la línea `id("kotlin-parcelize")`


```kotlin
plugins {
    id("kotlin-parcelize")
}
```

Luego, hacemos que nuestra clase `Category` sea `@Parcelize` y que implemente la interfaz `Parcelable`:

```kotlin
@Parcelize
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
) : Parcelable

```

Esto nos permite serializar el objeto `Category` para poder enviarlo a través de las diferentes pantallas.

# ✨ La importancia de la **serialización**

La importancia de la **serialización** y cómo utilizar la funcionalidad de **parcelables** para transferir objetos completos entre pantallas de una aplicación en **Kotlin**. Esto resulta útil cuando necesitamos trabajar con **datos complejos**, como objetos de clase.

### 📦 Serialización y Deserialización

La **serialización** es el proceso de convertir un objeto en un formato adecuado para ser almacenado o transmitido. En nuestro caso, convertimos un objeto en una cadena de texto que se puede enviar de una pantalla a otra de la aplicación.

Por ejemplo, en nuestra aplicación de **recetas**, intentamos pasar una **categoría completa** entre pantallas. Como la categoría no es un simple tipo de dato (como un **String**), sino un objeto complejo, necesitamos serializarlo para poder enviarlo correctamente.

El proceso inverso, la **deserialización**, permite convertir esa cadena de vuelta a un objeto para ser utilizado en el contexto adecuado.

### 🚀 Utilizando Parcelables en Kotlin

En Kotlin, podemos lograr la serialización utilizando el concepto de **parcelables**. Esto nos permite marcar una clase como **"parcelable"** y, de esta manera, los objetos de esa clase pueden ser enviados entre componentes de la aplicación.

#### Ejemplo: Categoría en la App de Recetas

Para enviar un objeto de tipo **categoría** de una pantalla a otra, debemos asegurarnos de que sea **parcelable**. A continuación, se muestra un ejemplo de cómo se hace esto en Kotlin:

```kotlin
@Parcelize
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
) : Parcelable
```

Al utilizar la anotación `@Parcelize`, indicamos que esta clase se puede serializar y deserializar fácilmente. Esto es especialmente útil cuando queremos **almacenar objetos complejos** en elementos como el **SaveStateHandle**.

