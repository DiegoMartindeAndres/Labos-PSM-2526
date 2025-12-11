# 🚀 Conexión entre Android y Firebase (Firestore)

En este laboratorio vamos a aprender cómo conectar nuestra aplicación Android con Firebase Firestore para guardar y recuperar notas en la nube. ☁️
Veremos cómo estructurar la capa de datos, cómo enviar información y cómo mostrarla en nuestra interfaz con Jetpack Compose.

# 📋 Tabla de Contenidos
- [🚀 Conexión entre Android y Firebase (Firestore)](#-conexión-entre-android-y-firebase-firestore)
- [📋 Tabla de Contenidos](#-tabla-de-contenidos)
- [🚀 Flujo general de la app](#-flujo-general-de-la-app)
- [💾 NotesRepository: Capa de Datos](#-notesrepository-capa-de-datos)
  - [Obtener notas: getNotes()](#obtener-notas-getnotes)
  - [Añadir nota: addNote()](#añadir-nota-addnote)
- [📱 MainActivity: Navegación Básica](#-mainactivity-navegación-básica)
- [📝 NewNoteScreen: Crear y Subir Nota](#-newnotescreen-crear-y-subir-nota)
- [📜 NotesListScreen: Leer y Mostrar Notas](#-noteslistscreen-leer-y-mostrar-notas)

# 🚀 Flujo general de la app

El funcionamiento de la aplicación se divide en dos pestañas principales:

1.  **Pestaña "Nueva"**:
    -   El usuario escribe un título y una lista de elementos (uno por línea).
    -   Pulsa el botón **"Guardar nota"**.
    -   Se llama a `NotesRepository.addNote(...)` que escribe los datos en Firestore.

2.  **Pestaña "Notas"**:
    -   Al entrar en la pantalla, se ejecuta `NotesRepository.getNotes()`.
    -   Se descargan los documentos de Firestore (de la colección `notes`).
    -   Se pintan en una lista utilizando `LazyColumn`.

La navegación se gestiona mediante una `BottomNavigationBar` que cambia un entero (`selectedTab`). Según su valor, Compose muestra una pantalla u otra.

# 💾 NotesRepository: Capa de Datos

Este componente actúa como nuestro **DAO** (Data Access Object) para Firestore. Es el encargado de hablar con la base de datos.

Para empezar, necesitamos la instancia de la base de datos:

```kotlin
private val db = Firebase.firestore
```

> **NOTA:** Este objeto es la puerta de entrada a Firestore. Si esta línea no lanza errores, significa que la app ya está correctamente conectada a tu base de datos.

## Obtener notas: getNotes()

La función para obtener las notas se define como `suspend` porque realiza operaciones de entrada/salida asíncronas.

```kotlin
suspend fun getNotes(): List<Note>
```

Necesitamos que sea asíncrona para poder usar funciones como `await()` y **no bloquear el hilo de UI**.

### 1. Traer los datos de Firebase 📥

```kotlin
db.collection("notes")...get().await()
```

Primero hay que entender cómo organiza Firestore sus jerarquías. Firestore siempre sigue una estructura alternada:

**colección → documento → colección → documento → ...**

Un documento puede contener:
-   Campos simples (`String`, `Number`, `Boolean`, `List`, `Map`…)
-   Subcolecciones (colecciones hijas dentro del documento)

Sabiendo esto, siempre podremos navegar correctamente hasta la lista de documentos que queramos. Esa línea obtiene todos los documentos de la colección `notes`.

<details>
<summary>¿Qué pasa cuando las rutas se complican?</summary>
<br>

Si tenemos la siguiente jerarquía (usada en el 90% de las apps):

```
usuarios/
   userId/
      perfil/
      notes/
         nota1
         nota2
```

Y queremos llegar hasta `notes`, la ruta sería:

```kotlin
db.collection("usuarios")
    .document(userId)
    .collection("notes")
```
</details>

La llamada devuelve un **snapshot**, que es la representación de los datos en ese momento.

### 2. Procesamiento de los datos crudos ⚙️

Usamos `map {}` para recorrer la lista y transformarla. Es como si fusionáramos un `foreach {}` con un `return List<>()` en una única lambda.

Por cada documento de la colección "notes", creamos un objeto `Note` con los campos que elijamos:
-   `"title"` → String
-   `"items"` → List<String>
-   `"createdAt"` → Timestamp generado automáticamente

## Añadir nota: addNote()

```kotlin
suspend fun addNote(title: String, items: List<String>)
```

Si has entendido `getNotes()`, entender esta es sencillo. Es la operación inversa: convertir el modelo en datos “crudos” y enviarlos a Firestore.

### 1. Convertimos modelo a datos crudos 📦

Firestore almacena estructuras tipo **clave → valor**, por lo que creamos un `Map` con lo que queremos subir:

```kotlin
val data = mapOf(
            "title" to title,
            "items" to items,
            "createdAt" to FieldValue.serverTimestamp()
        )
```

### 2. Enviamos los datos a la ruta 📤

```kotlin
db.collection("notes").add(data).await():
```

-   `.add(...)`: Inserta un documento nuevo **con ID automático**.
-   `.await()`: Bloquea la corrutina hasta que Firebase termine la operación.

# 📱 MainActivity: Navegación Básica

En el `MainActivity`, configuramos el punto de entrada y la navegación básica. Dentro de `setContent { ... }`:

```kotlin
val repo = remember { NotesRepository() }
var selectedTab by remember { mutableStateOf(0) }
```

-   `repo`: Se crea una vez por composición y se reutiliza.
-   `selectedTab`: Es tu “estado de navegación” (0 = crear, 1 = listar).

```kotlin
Scaffold(
    bottomBar = { NavigationBar { ... } }
) { innerPadding ->
    when (selectedTab) {
        0 -> NewNoteScreen(repo, Modifier.padding(innerPadding))
        1 -> NotesListScreen(repo, Modifier.padding(innerPadding))
    }
}
```

-   **Scaffold**: Nos da la estructura base (contenedor + bottom bar).
-   **Bottom Bar**: Cambia el valor de `selectedTab`.
-   **when (selectedTab)**: Decide qué Composable se pinta.

> **NOTA:** No usamos `NavController` aquí por simplicidad; es una navegación “manual” basada en estado.

# 📝 NewNoteScreen: Crear y Subir Nota

Esta pantalla gestiona el formulario para crear una nueva nota.

### Estado de la pantalla

```kotlin
var title by remember { mutableStateOf("") }
var itemsText by remember { mutableStateOf("") }
var message by remember { mutableStateOf<String?>(null) }
val scope = rememberCoroutineScope()
```

-   `title`: Campo de texto de 1 línea.
-   `itemsText`: Área de texto; cada línea será un elemento de la lista.
-   `message`: Feedback para el usuario.
-   `scope`: Para lanzar corrutinas desde la UI.

### Al pulsar el botón

Primero, una validación básica:

```kotlin
if (title.isBlank()) { 
    message = "El título no puede estar vacío"; return 
}
```

Luego, transformamos el texto multilínea en una `List<String>`:

```kotlin
val items = itemsText
    .lines()
    .map { it.trim() }
    .filter { it.isNotEmpty() }
```

### Subida a Firestore ☁️

```kotlin
scope.launch {
    try {
        repo.addNote(title, items)
        message = "Nota guardada correctamente"
        title = ""
        itemsText = ""
    } catch (e: Exception) {
        message = "Error al guardar: ${e.message}"
    }
}
```

-   Se lanza una **corrutina** (para no bloquear el hilo de UI).
-   Llama a la función `suspend` del repositorio.
-   **Si va bien**: Muestra mensaje de éxito y resetea los campos.
-   **Si falla**: Muestra el mensaje de error.

> **CLAVE:** Usamos `suspend + await()` en el repositorio, y `launch { ... }` en la UI.

# 📜 NotesListScreen: Leer y Mostrar Notas

Esta pantalla se encarga de descargar y listar las notas.

### Estado

```kotlin
var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
var isLoading by remember { mutableStateOf(false) }
var error by remember { mutableStateOf<String?>(null) }
```

-   `notes`: La lista que viene de Firestore.
-   `isLoading`: Para mostrar el spinner de carga.
-   `error`: Texto por si ocurre algún problema.

### Carga inicial 🔄

```kotlin
LaunchedEffect(Unit) {
    isLoading = true
    try {
        notes = repo.getNotes()
    } catch (e: Exception) {
        error = e.message ?: "Error desconocido"
    } finally {
        isLoading = false
    }
}
```

`LaunchedEffect(Unit)` se ejecuta una vez al entrar en el Composable y crea una corrutina donde se pueden llamar funciones `suspend` como `getNotes()`.

-   **try**: Intenta actualizar `notes`.
-   **catch**: Si `repo.getNotes()` falla, actualiza `error`.
-   **finally**: En cualquier caso, al terminar, pone `isLoading` a `false` para quitar el spinner.

### Pintado según estado 🎨

```kotlin
when {
    isLoading -> CircularProgressIndicator()
    error != null -> Text("Error: $error")
    notes.isEmpty() -> Text("No hay notas todavía")
    else -> LazyColumn { items(notes) { note -> ... } }
}
```

Aquí hay mucha lógica en pocas líneas. El `when` ejecuta la primera condición que sea `true` (en orden de arriba a abajo):

1.  **Cargando** -> Pinta el spinner.
2.  **Error** -> Muestra el texto del error.
3.  **Vacío** -> Si todo va bien pero no hay notas, muestra mensaje.
4.  **Con datos** -> Muestra la `LazyColumn` con las notas.

Si alguno de los campos de estado cambia, la UI se actualiza automáticamente. ✨

---

¡Espero que este laboratorio te ayude a conectar tu app con Firebase! Si tienes dudas, revisa los pasos anteriores. ¡A programar! 👨‍💻👩‍💻
