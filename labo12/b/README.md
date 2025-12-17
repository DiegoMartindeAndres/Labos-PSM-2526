# 🔥 SUBIENDO LA APLICACIÓN DE RECETAS A FIREBASE 🥘

Vamos a partir de la aplicación de recetas del labo anterior y vamos a añadir lógica para poder subir a la nube nuestras propias recetas custom

⚠️ **ADVERTENCIA:** En este labo se le han cambiado el nombre a algunas clases, separado funciones o `@Composables` en varios archivos para especificar su propósito, mejorar su lectura o simplemente encontrarla con facilidad. Por ejemplo CategoryItem se ha movido a su propio archivo y `RecipesViewModel` se ha renombrado a `RecipesVM`


## Ficheros de inicio
(En esta dirección puedes descargarte los ficheros de inicio de la aplicación.)[./src/]

Cada vez que se explique cómo se hace x en el labo se explicará en orden de construcción, por ejemplo: al explicar cómo se sube algo a firebase se dividirá en **Repository -> ViewModel -> View**

## ☁️ Lógica de Firebase
Antes de que pretendamos subir nada a firebase tenemos que crear nuestro proyecto Firebase, conectar la aplicación y añadirle la base de datos Firestore tanto en la consola como en la aplicación

<hr>

Crear un proyecto en Firebase y conectar una aplicación a él es un proceso sencillo, delicado **Y COMPLETAMENTE GRATUITO** donde un pequeño error en la configuración inicial (como registrar mal el paquete, colocar mal el archivo de configuración o añadir mal una dependencia) puede provocar fallos difíciles de rastrear más adelante e incluso devolverte a la casilla 0; por eso es importante seguir los pasos con cuidado y asegurarse de que todo queda correctamente configurado antes de empezar a desarrollar la lógica de la app.

Vamos con ello. Primero de todo: https://firebase.google.com
 → `Get started` (o `Ir a la consola`) → nos hará iniciar sesión (si no lo estamos ya) → `Crear un proyecto de Firebase nuevo`.

Comenzamos con la creación del proyecto en firebase, yo he decidido llamarlo **FireStove**

<p align="center">
  <img src="imgs/image057.png" width="80%"/>
</p>

Ahora no hemos hecho nada más que hacerle hueco a donde va a ir nuestra app, ahora toca añadirla:

<p align="center">
  <img src="imgs/image061.png" width="40%"/>
</p>

Click en "Android"

<p align="center">
  <img src="imgs/image062.png" width="40%"/>
</p>

y nos topamos con esta pantalla que nos pide el paquete de nuestra app

<p align="center">
  <img src="imgs/image063.png" width="40%"/>
</p>

lo podéis encontrar en varios sitios pero hay 2 muy claros:
-	En la vista de paquetes

<p align="center">
  <img src="imgs/image064.png" width="90%"/>
</p>

-	En build.gradle.kts(Module :app)
 
<p align="center">
  <img src="imgs/image066.png" width="90%"/>
</p>

Ahora nos descargamos el google-services.json y siguiendo los pasos movemos el archivo descargado al directorio raíz de la app (a la altura de `app`) y le damos a siguiente

<p align="center">
  <img src="imgs/image080.png" width="90%"/>
</p>

Ahora toca una parte delicada pero sencilla: sigue los pasos que indica Firebase e introduce las líneas en los archivos que te marca. Aquí existe una herramienta de Android Studio que intenta hacerlo automáticamente, pero a veces lo hace mal; por eso Firebase recomienda seguir los pasos manualmente.

Las líneas de Firebase añaden las dependencias mínimas para usar Firebase en la app y una opcional que es Google Analytics (para ver estadísticas de uso). Pero nos falta la dependencia de **Firestore**.

En vuestro `build.gradle.kts (Module: app)` vais a añadir la siguiente línea:

```kotlin
implementation ("com.google.firebase:firebase-firestore")
```

Le damos a sync now y nos debería aparecer un mensaje con `BUILD SUCCESFUL IN xs`

Volvemos a firebase, le damos a siguientente y luego “ir a la consola” sin tocar nada más

<p align="center">
  <img src="imgs/image070.png" width="90%"/>
</p>

Esta es la consola de firebase, desde donde podemos gestionar nuestra aplicación, añadirle herramientas, tocar datos, borrarla... :

<p align="center">
  <img src="imgs/image073.png" width="90%"/>
</p>

En pasos anteriores, en la aplicación hemos traído la dependencia de firestore pero nuestra aplicación de firebase no lo sabe todavía a si que hay que actualizarla. Nos vamos a ir a Compilación/Firestore Database

<p align="center">
  <img src="imgs/image074.png" width="45%"/>
</p>

Le damos al botón Crear base de datos

<p align="center">
  <img src="imgs/image075.png" width="45%"/>
</p>

Edición Standard

<p align="center">
  <img src="imgs/image076.png" width="90%"/>
</p>

ID de la base de datos: (default)

Ubicación: europe-southwest1 (Madrid)

<p align="center">
  <img src="imgs/image078.png" width="90%"/>
</p>

Configuramos la base de datos ya en modo de producción, en el modo de prueba las reglas (que veremos más tarde) son temporales y hay que actualizarlas cada 30 días, no queremos eso

<p align="center">
  <img src="imgs/image081.png" width="90%"/>
</p>

Y ya hemos llegado a una vista que vamos a visitar muy a menudo:

<p align="center">
  <img src="imgs/image082.png" width="90%"/>
</p>

Aquí nos irán apareciendo las recetas que vayamos añadiendo desde la app. Podremos crearlas a mano desde aquí (no tiene mucho sentido), editar las que vengan de la aplicación o incluso borrarlas.

Pero ahora mismo, por defecto, en esta base de datos nadie puede hacer nada por las reglas que vienen activas. Vamos a cambiar eso.

Nos vamos a ir al apartado de `Reglas` y vamos a quitar las que hay y  vamos a cambiar el `false` por `true` y le damos a **Publicar**, quedando algo así:

<p align="center">
  <img src="imgs/image084.png" width="90%"/>
</p>

Con esto acabamos de hacer nuestra base de datos **pública**, porque las reglas nos dejan hacer lo que queramos con ella, seamos quienes seamos, desde dentro o desde fuera de la app. Por eso sale ese precioso mensaje en rojo avisando de que lo que estamos haciendo es peligroso:

<p align="center">
  <img src="imgs/image085.png" width="90%"/>
</p>

La configuración de la base de datos en la consola termina aquí finalmente, ahora nos vamos a volver a kotlin para decirle a la app cómo y cuando se escriben y se leen los datos.


## 🚀 Subir una categoría y una receta personalizada a Firebase

En esta parte vamos a ver cómo firestore gestiona las rutas, los datos, cómo hacer CRUD con estos, los eventos a los que nos podemos subscribir...

## 🛣️ ¿Cómo gestiona Firestore las rutas?

Firestore organiza los datos en **Colecciones** y **Documentos**. Piensa en las colecciones como carpetas y en los documentos como archivos dentro de esas carpetas. La regla de oro es que siempre debes alternar: `Colección -> Documento -> Colección -> Documento`.

Por ejemplo:
```kotlin
// Referencia a una colección (contiene documentos)
val recipesRef = db.collection("recipes")

// Referencia a un documento específico dentro de esa colección
val myRecipeRef = db.collection("recipes").document("receta_123")

// Referencia a una subcolección (dentro de un documento)
val ingredientsRef = db.collection("recipes").document("receta_123").collection("ingredients")
```
No puedes guardar datos directamente en una colección, ni crear una colección dentro de otra colección sin pasar por un documento.

## 💾 ¿Cómo gestiona Firestore los datos?

Firestore guarda la información en documentos, que funcionan de forma muy parecida a un JSON. Cada documento es un conjunto de pares **clave-valor**.

Puedes guardar tipos de datos simples y complejos:
*   **Simples:** `String`, `Number`, `Boolean`.
*   **Complejos:** `Array` (listas), `Map` (objetos anidados), `Timestamp` (fechas), `Geopoint`, etc.

En Kotlin, para enviar datos a Firestore, solemos crear un `Map` con los valores que queremos guardar.

Ejemplo de cómo se ve un objeto listo para subir:
```kotlin
val recipeData = mapOf(
    "name" to "Tortilla de Patatas",
    "difficulty" to "Easy",
    "ingredients" to listOf("Huevos", "Patatas", "Cebolla"), // Array
    "isVegetarian" to true, // Boolean
    "stats" to mapOf("likes" to 10, "views" to 500), // Map anidado
    "createdAt" to FieldValue.serverTimestamp() // Timestamp del servidor
)
```
Firestore es "schemaless" (sin esquema o no-SQL), lo que significa que no necesitas definir las columnas de antemano como en SQL. Dos documentos en la misma colección pueden tener campos diferentes (aunque por salud mental, intentamos que sean iguales).

## 🤔 ¿Qué tenemos que hacer ahora que sabemos cómo funciona Firestore?

- ⭐ Subir a firebase una categoría a modo de "favorita" (solo creación)
- 👨‍🍳 Poder crear recetas personalizadas, verlas y actualizarlas (CRUD)

## ⭐ Subida de una categoría a Firebase

En esta sección se explica cómo se sube una categoría a firebase firestore cada vez que le demos al botón de **favorito** desde la app a una colección llamada `colecciones_favoritas`

### 🗄️ Repository

El repositorio es el encargado de hablar directamente con Firebase. Aquí usamos un `object` (Singleton) para no tener que instanciarlo cada vez.

Archivo: `FavoritesRepository.kt`

```kotlin
object FavoritesRepository {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore
    // Referencia a la colección donde guardaremos los favoritos
    private val favsCollection = db.collection("colecciones_favoritas")

    fun addFavoriteCategory(
        category: Category,
        onResult: (Boolean, String?) -> Unit // Callback para avisar al VM si salió bien o mal
    ) {
        // 1. Preparamos los datos en un Mapa (clave-valor)
        val data = mapOf(
            "name" to category.strCategory,
            "thumb" to category.strCategoryThumb,
            "description" to category.strCategoryDescription,
            "createdAt" to FieldValue.serverTimestamp() // Marca de tiempo del servidor
        )

        // 2. Escribimos en Firestore
        // .document() sin argumentos genera un ID aleatorio automático.
        // Si quisieras usar el nombre como ID (para evitar duplicados), usarías .document(category.strCategory)
        favsCollection.document()
            .set(data)
            .addOnSuccessListener { 
                // Todo OK
                onResult(true, null) 
            }
            .addOnFailureListener { e -> 
                // Hubo un error
                onResult(false, e.message) 
            }
    }
}
```

**Puntos clave del código:**
1.  **`db.collection("colecciones_favoritas")`**: Apuntamos a la "carpeta" donde queremos guardar las cosas. Si no existe, Firestore la crea sola.
2.  **`mapOf(...)`**: Transformamos nuestro objeto `Category` (que es una clase de Kotlin) a un mapa genérico que Firestore entienda.
3.  **`.document().set(data)`**:
    *   `.document()`: Crea una referencia a un documento nuevo con un ID único autogenerado (ej: `y7d8s9f7s8d`).
    *   `.set(data)`: Guarda el mapa de datos en ese documento.
4.  **Callbacks (`onResult`)**: Como la operación es asíncrona (tarda un poco en ir a internet y volver), usamos una función lambda para avisar cuando termine.


### 🧠 ViewModel

El ViewModel actúa de intermediario. Recibe la orden de la vista ("el usuario pulsó favorito") y llama al repositorio. También gestiona el estado de la UI (por ejemplo, mostrar un mensaje de "Guardado" o "Error").

Archivo: `FavoritesVM.kt`

```kotlin
class FavoritesVM : ViewModel() {

    // Estado para mostrar mensajes (Toast) en la vista
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun addFavorite(category: Category) {
        // Llamamos al repositorio
        FavoritesRepository.addFavoriteCategory(category) { ok, msg ->
            // Actualizamos el estado con el resultado
            _message.value =
                if (ok) "Añadida a favoritas"
                else msg ?: "Error al guardar"
        }
    }

    // Limpiamos el mensaje después de mostrarlo para no repetirlo
    fun clearMessage() {
        _message.value = null
    }
}
```

**¿Qué pasa aquí?**
*   Usamos `MutableStateFlow` para comunicar eventos de una sola vez (como un Toast) a la vista.
*   Cuando el repositorio responde (`ok` o error), actualizamos `_message.value`. La vista estará "escuchando" este valor.

### 📱 GUI (Vista)

En la vista (`CategoryItem`), inyectamos el ViewModel y observamos el estado para mostrar feedback al usuario.

Archivo: `CategoryItem.kt`

```kotlin
@Composable
fun CategoryItem(
    category: Category,
    navigateToDetail: (Category) -> Unit
) {
    // 1. Obtenemos una instancia del ViewModel
    val favoritesVM: FavoritesVM = viewModel()
    
    // 2. Observamos el estado del mensaje
    val message by favoritesVM.message.collectAsState()
    val context = LocalContext.current

    // 3. Reaccionamos a cambios en 'message' (Efecto secundario)
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            favoritesVM.clearMessage() // Reseteamos para que no salga otra vez al rotar pantalla
        }
    }

    // ... resto de la UI ...

    // 4. Botón que dispara la acción
    IconButton(onClick = { favoritesVM.addFavorite(category) }) {
        Icon(Icons.Default.Star, contentDescription = "Favorito")
    }
}
```

**Flujo completo:**
1.  Usuario pulsa el botón ⭐.
2.  `onClick` llama a `favoritesVM.addFavorite(category)`.
3.  VM llama a `FavoritesRepository.addFavoriteCategory`.
4.  Repo guarda en Firestore y avisa al VM (`onResult`).
5.  VM actualiza `_message` con "Añadida a favoritas".
6.  La Vista detecta el cambio en `message` gracias a `collectAsState`.
7.  `LaunchedEffect` se dispara y muestra el `Toast`.

## 👨‍🍳 Subida de una receta personalizada a Firebase

Queremos que el usuario pueda crear sus propias recetas. Para ello, necesitamos un formulario (GUI), un ViewModel que gestione la lógica y un Repository que hable con Firestore.

### 🗄️ Repository

En `RecipesRepository.kt`, añadimos la función `uploadRecipe`.

```kotlin
object RecipesRepository {
    // ... db y collection ...

    fun uploadRecipe(
        recipe: PersonalList,
        onResult: (Boolean, String?) -> Unit
    ) {
        // Convertimos el objeto PersonalList a un Map para Firestore
        val data = mapOf(
            "nombre" to recipe.nombre,
            "ingredientes" to recipe.ingredientes,
            "description" to recipe.description,
            "precio" to recipe.precio,
            "createdAt" to FieldValue.serverTimestamp()
        )

        recipesCollection
            .add(data) // .add() crea un documento con ID automático
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }
}
```

### 🧠 ViewModel

En `RecipesVM.kt`, creamos la función que llamará la vista.

```kotlin
class RecipesVM : ViewModel() {
    // ...

    fun createRecipe(
        recipe: PersonalList,
        onResult: (Boolean, String?) -> Unit
    ) {
        RecipesRepository.uploadRecipe(recipe) { ok, msg ->
            onResult(ok, msg)
            // Si sale bien, podríamos recargar la lista de recetas aquí
            if (ok) loadRecipes() 
        }
    }
}
```

### 📱 GUI

Necesitamos una pantalla con un formulario.

**1. Definir la ruta en `Screen.kt`**
```kotlin
data object NewRecipe : Screen("new_recipe")
```

**2. Crear la pantalla `NewRecipeScreen.kt`**
Esta pantalla recoge los datos (nombre, ingredientes, etc.) y llama a la función de subir.

<details>
<summary>Ver código entero de NewRecipeScreen.kt (copiar y pegar)</summary>

```kotlin
// ... imports ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeScreen(
    uploadToFirebase: (PersonalList) -> Unit, // Callback para subir
    onBack: (() -> Unit)? = null
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var ingredientesText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva receta") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la receta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Ingredientes (opcional)
            OutlinedTextField(
                value = ingredientesText,
                onValueChange = { ingredientesText = it },
                label = { Text("Ingredientes (uno por línea, opcional)") },
                placeholder = { Text("Ej.:\n200g pasta\nTomate\nQueso rallado") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 6
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 6
            )

            // Precio
            OutlinedTextField(
                value = precioText,
                onValueChange = { nuevo ->
                    // Sólo permitimos números y un punto/coma decimal
                    if (nuevo.isEmpty() || nuevo.matches(Regex("""\d*([.,]\d*)?"""))) {
                        precioText = nuevo.replace(',', '.') // normalizamos a punto
                    }
                },
                label = { Text("Precio (€)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de guardar
            Button(
                onClick = {
                    // Validación
                    if (nombre.isBlank() || descripcion.isBlank() || precioText.isBlank()) {
                        errorMessage =
                            "Nombre, descripción y precio son obligatorios."
                        return@Button
                    }

                    val precio = precioText.toDoubleOrNull()
                    if (precio == null) {
                        errorMessage = "Introduce un precio numérico válido."
                        return@Button
                    }

                    val ingredientesList = ingredientesText
                        .lines()
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                    val nuevaReceta = PersonalList(
                        nombre = nombre.trim(),
                        ingredientes = ingredientesList,
                        description = descripcion.trim(),
                        precio = precio
                    )

                    errorMessage = null
                    // Llamamos a la función que nos pasaron por parámetro
                    uploadToFirebase(nuevaReceta)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Guardar receta")
            }
        }
    }
}
```
</details>

<br>

**3. Conectar todo en `RecipeApp.kt`**
Aquí es donde instanciamos el ViewModel y le pasamos la función `createRecipe` a la pantalla `NewRecipeScreen`.

```kotlin
composable(route = Screen.NewRecipe.route) {
    NewRecipeScreen(
        uploadToFirebase = { receta ->
            // Llamamos al ViewModel
            recipesVM.createRecipe(receta) { ok, errormsg ->
                if (ok) {
                    Toast.makeText(context, "Receta guardada correctamente", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Volver atrás si quieres
                } else {
                    Toast.makeText(context, errormsg ?: "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onBack = { navController.popBackStack() }
    )
}
```

**4. Añadir acceso en la `BottomNavBar`**
Para poder llegar a esta pantalla, añadimos un botón en la barra de navegación.

```kotlin
NavigationBarItem(
    selected = false,
    onClick = { navController.navigate(Screen.NewRecipe.route) },
    label = { Text("Nueva receta") },
    icon = { Icon(Icons.Default.Add, contentDescription = null) }
)
```

El resultado esperado es este:

<p align="center">
  <img src="imgs/image055.png" width="40%"/>
</p>

<br>

Esto ya es completamente funcional, es deicr, si rellenas los campos obligatorios y te vas a firebase verás tu primera receta:

<p align="center">
  <img src="imgs/image087.png" width="45%"/>
</p>
<p align="center">
  <img src="imgs/image089.png" width="95%"/>
</p>



## 📥 Fetch de las recetas

No tendría mucho sentido nuestra aplicación si solo sirve para crear recetas que luego no vamos a volver a ver, así que vamos a crear la **lógica de lectura** y una vista nueva.

En `RecipesRepository` la función `getRecipes()` devuelve una lista de `RecipeDoc` (cada documento contiene `id` y `data: PersonalList`):

```kotlin
fun getRecipes(
    onResult: (List<RecipeDoc>) -> Unit,
    onError: (Exception) -> Unit
) {
    recipesCollection
        .get()
        .addOnSuccessListener { snapshot ->
            try {
                val list = snapshot.documents.mapNotNull { doc ->
                    val recipe = doc.toObject(PersonalList::class.java) ?: return@mapNotNull null
                    RecipeDoc(doc.id, recipe)
                }
                onResult(list)
            } catch (e: Exception) {
                onError(e)
            }
        }
        .addOnFailureListener(onError)
}
```

### ViewModel

En `RecipesVM.kt` necesitamos una función que llame al repositorio y actualice el estado de la UI (`uiState`).

```kotlin
fun loadRecipes() {
    // 1. Ponemos loading a true
    uiState = uiState.copy(isLoading = true, error = null)

    // 2. Llamamos al repositorio
    RecipesRepository.getRecipes(
        onResult = { list ->
            // 3. Si sale bien, actualizamos la lista
            uiState = RecipesUiState(
                isLoading = false,
                recipes = list,
                error = null
            )
        },
        onError = { e ->
            // 4. Si sale mal, mostramos el error
            uiState = RecipesUiState(
                isLoading = false,
                recipes = emptyList(),
                error = e.message ?: "Error al cargar recetas"
            )
        }
    )
}
```

<br>

En el UI usamos `RecipesVM` que expone `uiState: RecipesUiState` y un método `loadRecipes()` para cargar los datos; cada elemento es `RecipeDoc`.

Ejemplo simplificado de `MyRecipesScreen` acorde al código actual:

```kotlin
@Composable
fun MyRecipesScreen(recipesVM: RecipesVM = viewModel()) {
    val state = recipesVM.uiState
    var editing by remember { mutableStateOf<RecipeDoc?>(null) }

    LaunchedEffect(Unit) { recipesVM.loadRecipes() }

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${state.error}")
        }

        state.recipes.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Todavía no has creado ninguna receta")
        }

        else -> LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(state.recipes) { doc ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(doc.data.nombre)
                    Text(doc.data.description)
                    Text("Ingredientes: ${doc.data.ingredientes.joinToString()}")
                    Text("Precio: ${doc.data.precio} €")
                    Row {
                        IconButton(onClick = { editing = doc }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                        IconButton(onClick = { recipesVM.deleteRecipe(doc.id) }) { Icon(Icons.Default.Delete, contentDescription = "Borrar") }
                    }
                }
            }
        }
    }

    // diálogo de edición: al confirmar llamar a recipesVM.updateRecipe(id, nuevo)
}
```

Añadimos la ruta en `Screen.kt`

```kotlin
data object MyRecipes : Screen("myRecipes")

NavHost en RecipeApp
composable(Screen.MyRecipes.route) {
    MyRecipesScreen()
}

Nuevo botón en BottomNavBar
NavigationBarItem(
    selected = false,
    onClick = { navController.navigate(Screen.MyRecipes.route) },
    label = { Text("Mis recetas") },
    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) } // o el icono que prefieras
)
```

Y con eto ya deberías poder ver las recetas que subiste a firestore:

<p align="center">
  <img src="imgs/image091.png" width="45%"/>
</p>

(yo añadí más recetas para probar que efectivamente me traía varias y no una única sola o que la vista no pintara la lista)

## ✏️ Editar una receta

Para editar, usamos un **diálogo emergente (`AlertDialog`)**. Cuando pulsamos el botón de editar en una receta, guardamos esa receta en una variable de estado llamada `editing`. Si esa variable no es nula, mostramos el diálogo.

### ViewModel

En `RecipesVM.kt`:

```kotlin
fun updateRecipe(id: String, newRecipe: PersonalList) {
    RecipesRepository.updateRecipe(id, newRecipe) { ok, msg ->
        if (ok) {
            // Actualizamos la lista localmente reemplazando el elemento modificado
            uiState = uiState.copy(
                recipes = uiState.recipes.map { 
                    if (it.id == id) RecipeDoc(id, newRecipe) else it 
                }
            )
        } else {
            uiState = uiState.copy(error = msg ?: "Error actualizando receta")
        }
    }
}
```

> **¿Qué es una variable de estado?**
> En Jetpack Compose, una variable de estado (`mutableStateOf`) es una variable "especial" que, cuando cambia su valor, avisa a la interfaz para que se vuelva a pintar (recomposición) con los nuevos datos. Usamos `remember` para que el valor no se pierda cada vez que la función se vuelve a ejecutar.

En `MyRecipesScreen.kt`:

```kotlin
// Estado para saber qué receta estamos editando (null = ninguna)
var editing by remember { mutableStateOf<RecipeDoc?>(null) }

// ... dentro del LazyColumn ...
IconButton(onClick = { editing = doc }) { 
    Icon(Icons.Default.Edit, contentDescription = "Editar") 
}

// ... fuera del LazyColumn, el código del diálogo ...
editing?.let { ed ->
    // Estados temporales para el formulario del diálogo
    var nombre by remember(ed.id) { mutableStateOf(ed.data.nombre) }
    var descripcion by remember(ed.id) { mutableStateOf(ed.data.description) }
    // ... otros campos ...

    AlertDialog(
        onDismissRequest = { editing = null }, // Cerrar al pulsar fuera
        title = { Text("Editar receta") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                // ... resto de campos ...
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // 1. Crear el objeto actualizado
                val updatedList = PersonalList(
                    nombre = nombre,
                    description = descripcion,
                    // ...
                )
                // 2. Llamar al VM
                recipesVM.updateRecipe(ed.id, updatedList)
                // 3. Cerrar diálogo
                editing = null
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { editing = null }) { Text("Cancelar") }
        }
    )
}
```

## 🗑️ Borrar una receta

El borrado es más directo. Simplemente llamamos a la función del ViewModel pasando el ID del documento.

En `MyRecipesScreen.kt`:

```kotlin
IconButton(onClick = { 
    // Llamada directa al ViewModel
    recipesVM.deleteRecipe(doc.id) 
}) { 
    Icon(Icons.Default.Delete, contentDescription = "Borrar") 
}
```

### ¿Cómo se actualiza la lista automáticamente?

Fíjate que en el ViewModel (`RecipesVM`), cuando borramos o editamos, actualizamos manualmente el estado local `uiState`. Esto hace que la pantalla se repinte sola sin tener que volver a pedir los datos a Internet.

**Ejemplo del borrado en el VM:**
```kotlin
fun deleteRecipe(id: String) {
    RecipesRepository.deleteRecipe(id) { ok, msg ->
        if (ok) {
            // Filtramos la lista actual quitando el que acabamos de borrar
            uiState = uiState.copy(
                recipes = uiState.recipes.filterNot { it.id == id }
            )
        }
        // ...
    }
}
```

## 🆚 `object` vs `class` (singletons y por qué importa)

Rápido y claro: en Kotlin `object` declara un singleton (una única instancia que vive durante toda la ejecución), mientras que `class` define un tipo que debes instanciar (puedes crear muchas instancias).

En este proyecto verás cosas como:

```kotlin
// singleton (instancia única)
object RecipesRepository { /*...*/ }

// clase (requiere instanciar)
class RecipesVM : ViewModel() { /*...*/ }
```

## ¿Por qué se usó object en los repositorios?

- Es cómodo: puedes llamar a `RecipesRepository.uploadRecipe(...)` desde cualquier sitio sin pasar la instancia.
- Inicialización perezosa y segura en concurrencia: Kotlin se encarga de crear la instancia cuando se accede por primera vez.

Sin embargo, usar singletons tiene efectos secundarios que conviene conocer:
- Estado global: si guardas estado mutable dentro del `object`, se comparte en toda la app y puede provocar efectos difíciles de depurar.
- Testabilidad: las `object` son más complicadas de mockear sin herramientas (aunque hay técnicas y libs que lo permiten). Las `class` inyectadas son más fáciles de sustituir en tests.
- Ciclo de vida: un `object` no está ligado al ciclo de componentes Android (Activity/VM/Scope). Si necesitas liberar recursos en un momento concreto, una `class` con lifecycle/DI es mejor.

Tabla rápida de pros/cons

| Enfoque | Pros | Contras |
|---|---|---|
| `object` (singleton) | Acceso global simple; inicialización perezosa/thread-safe; menos boilerplate | Estado global compartido, más difícil de testear, menor control de ciclo de vida |
| `class` (instanciable) | Fácil de inyectar/testear; mayor control del ciclo de vida; no hay estado global implícito | Requiere pasar/gestionar instancias, más boilerplate sin DI |


