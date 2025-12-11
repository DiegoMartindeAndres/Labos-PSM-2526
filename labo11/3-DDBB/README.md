# 📱 Aplicación Lista de deseos con `Room Database`

En el laboratorio anterior nos preocupamos de las vistas pero en este laboratorio vamos a preocuparnos de la lógica de la aplicación. Vamos a aprender a guardar datos de manera permanente usando **Room Database**. 

La aplicación que vamos a desarrollar tendrá el siguiente aspecto.
Permitirá actulizar deseos, añadir deseos, eliminar deseos usando el gesto `swipe to delete` y ver los detalles de un deseo. Todo almacenado en una base de datos local.

<div align="center">
    <img src="img/app.gif" alt="Ejemplo app.">
</div>

# 📚 Tabla de Contenidos
- [📱 Aplicación Lista de deseos con `Room Database`](#-aplicación-lista-de-deseos-con-room-database)
- [📚 Tabla de Contenidos](#-tabla-de-contenidos)
- [Configuración de la Clase de Aplicación 🌐](#configuración-de-la-clase-de-aplicación-)
- [Código Completo de la Aplicación](#código-completo-de-la-aplicación)

## Introducción a las Operaciones CRUD con Room Database

En este apartado, vamos a aprender a implementar una base de datos en nuestra aplicación usando **Room Database**. Esta funcionalidad es esencial para cualquier desarrollador Android, ya que permite almacenar datos de manera persistente, asegurando que los datos estén disponibles incluso después de cerrar la aplicación.

## 🗂️ Guardando datos localmente en Android

En este laboratorio aprenderemos a guardar datos localmente en Android utilizando diferentes enfoques. La aplicación que desarrollaremos implementará una base de datos utilizando la biblioteca **Room Database**.

## 📝 Opciones para guardar datos localmente

Existen varias opciones para almacenar datos localmente en un dispositivo Android:

1. **Shared Preferences**:
   - Sistema de almacenamiento clave-valor.
   - Ideal para guardar pequeñas cantidades de datos primitivos como cadenas, enteros y booleanos.
   - Uso común: almacenar preferencias del usuario o configuraciones de la aplicación.

2. **Almacenamiento Interno**:
   - Sistema de archivos específico de la aplicación y accesible solo por esta.
   - Ideal para guardar archivos privados que no deben ser accesibles por otras aplicaciones o usuarios.

3. **Almacenamiento Externo**:
   - Refleja almacenamiento fuera del dispositivo, como una tarjeta SD o almacenamiento USB.
   - Ideal para guardar archivos que se compartirán con otras aplicaciones o usuarios.

4. **SQLite y Room**:
   - Diseñado para almacenar datos estructurados.
   - Room simplifica el uso de SQLite, proporcionando una capa de abstracción.

## ⚖️ Elegir la opción adecuada

La elección del método de almacenamiento depende de las necesidades específicas de la aplicación:
- Para datos pequeños que no necesitan una base de datos estructurada, usa **Shared Preferences**.
- Para datos estructurados y consultas complejas, usa **SQLite** o **Room**.

## 🛠️ Introducción a Room

Room es una biblioteca que facilita el trabajo con bases de datos SQLite en Android. Proporciona una capa de abstracción que permite interactuar con la base de datos mediante objetos de acceso a datos (DAO).

### Características clave de Room:
- Proporciona una clase de entidad.
- Incluye un objeto de acceso a datos (DAO).
- Facilita operaciones CRUD (crear, leer, actualizar, eliminar).

### 💾 Componentes de Room

1. **Clase de entidad**:
   - Representa una tabla en la base de datos.
   - Ejemplo: `Wish`, donde cada instancia representa una fila en la tabla.

2. **DAO (Data Access Object)**:
   - Define las operaciones CRUD.
   - Ejemplo de operaciones:
     - Insertar un deseo.
     - Buscar todos los deseos en una tabla.
     - Buscar un deseo por su ID.

3. **Clase de base de datos**:
   - Extiende `RoomDatabase`.
   - Vincula las entidades y DAOs definidos.

## 🌊 Uso de Flows
Room también permite trabajar con `Flow` para manejar datos de manera reactiva. Esto se explorará en mayor detalle en vídeos futuros.

## Configuración de la Clase `Wish` como Entidad 📚

En este apartado, prepararemos nuestra clase de datos `Wish` para convertirla en una entidad que podamos utilizar con nuestro DAO (Data Access Object) y la base de datos Room.

### Paso 1: Anotación de Entidad

1. Para que nuestra clase de datos `Wish` pueda ser almacenada de manera permanente en el dispositivo mediante Room, añadimos la anotación `@Entity`.

```kotlin
   @Entity(tableName="wish-table")
   data class Wish(
       @PrimaryKey(autoGenerate = true)
       val id: Long = 0L,
       @ColumnInfo(name="wish-title")
       val title: String="",
       @ColumnInfo(name="wish-desc")
       val description: String=""
   )
```
   - **`@Entity`**: Convierte la clase en una tabla de base de datos. El atributo `tableName` define el nombre de la tabla.

### Paso 2: Definición de la Clave Primaria

1. Añadimos la anotación `@PrimaryKey` a la propiedad que será nuestra clave primaria:
   ```kotlin
   @PrimaryKey(autoGenerate = true)
   val id: Long = 0L
   ```
   - **`autoGenerate = true`**: Hace que Room genere automáticamente un valor único e incremental para cada nueva entrada.

### Paso 3: Definición de las Columnas

1. Usamos la anotación `@ColumnInfo` para especificar los nombres de las columnas:
   ```kotlin
   @ColumnInfo(name="wish-title")
   val title: String=""

   @ColumnInfo(name="wish-desc")
   val description: String=""
   ```
   - **`@ColumnInfo`**: Permite definir nombres específicos para las columnas en la base de datos.

### Estructura de la Tabla

| Columna           | Nombre en la Base de Datos | Tipo       | Descripción                          |
|-------------------|----------------------------|------------|--------------------------------------|
| **ID**            | `id`                       | `Long`     | Clave primaria, generada automáticamente |
| **Título**        | `wish-title`               | `String`   | Título del deseo                     |
| **Descripción**    | `wish-desc`                | `String`   | Descripción del deseo                 |


### Comportamiento de la Clave Primaria

La clave primaria **ID** asegura que cada fila en la tabla sea única. Incluso si eliminamos una fila, el siguiente ID siempre incrementará en uno. Por ejemplo:

- Si eliminamos el deseo con ID 3, el siguiente deseo creado tendrá el ID 4.

### Representación Visual de la Base de Datos

Podemos visualizar la base de datos como una hoja de cálculo:

- **Base de Datos:** Representada por el archivo.
- **Tabla:** Representa la estructura de datos (`wish-table`).
- **Columnas:** Los atributos `id`, `wish-title`, `wish-desc`.
- **Filas:** Cada fila representa una entrada (un deseo).

Ejemplo:
```plaintext
| ID  | wish-title       | wish-desc                       |
|-----|------------------|---------------------------------|
| 1   | Honor Magic V2   | Smartphone plegable impresionante |
| 2   | Coche            | Coche nuevo y rápido Speedy Gonzalez |
| 3   | Casa             | Casa grande, tranquila y céntrica |
| 4   | Bicicleta eléctrica| Bici rápida y de largo alcance    |
```

### Estructura del DAO: Configuración inicial 🛠️

En este apartado vamos a configurar la clase **DAO (Data Access Object)**, que nos permite realizar las operaciones CRUD ( Create, Read, Update, Delete ) en la base de datos de nuestra aplicación.

---

### Paso 1: Creación de la clase DAO

Creamos un archivo en el paquete `data` de nuestro proyecto y lo llamamos `WishDao.kt`. Este archivo contendrá una clase abstracta que define los métodos necesarios para interactuar con nuestra base de datos.

```kotlin
@Dao
abstract class WishDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addAWish(wishEntity: Wish)

    // Loads all wishes from the wish table
    @Query("Select * from `wish-table`")
    abstract fun getAllWishes(): Flow<List<Wish>>

    @Update
    abstract suspend fun updateAWish(wishEntity: Wish)

    @Delete
    abstract suspend fun deleteAWish(wishEntity: Wish)

    @Query("Select * from `wish-table` where id=:id")
    abstract fun getAWishById(id:Long): Flow<Wish>


}
```

Vamos a explicar paso por paso lo que quiere decir cada método.

### Paso 2: Inserción de un registro (Create) ✨

Usamos la anotación `@Insert` para definir un método de inserción. Este método usa una estrategia de manejo de conflictos para determinar qué hacer si intentamos insertar un registro duplicado.

```kotlin
@Insert(onConflict = OnConflictStrategy.IGNORE)
abstract suspend fun addAWish(wishEntity: Wish)
```

- **onConflict**: Estrategia que define qué hacer en caso de conflicto (por ejemplo, ignorar el conflicto). Las opciones disponibles en `OnConflictStrategy` son:
  - **REPLACE**: Reemplaza el registro existente con el nuevo.
  - **IGNORE**: Ignora el nuevo registro si ya existe un conflicto.
  - **ABORT**: Cancela la operación de inserción, lanzando una excepción.
  - **ROLLBACK**: Revierte la transacción actual.
  - **FAIL**: Falla la operación de inserción sin revertir la transacción.
- **suspend**: El método se marca como `suspend` para que se ejecute en un hilo en segundo plano, usando corrutinas. Esto permite que la operación de inserción no bloquee el hilo principal.

### Paso 3: Lectura de todos los registros (Read) 📜

Para cargar todos los registros de la tabla, usamos la anotación `@Query` con una instrucción SQL simple.

```kotlin
@Query("Select * from `wish-table`")
abstract fun getAllWishes(): Flow<List<Wish>>
```

- **Flow**: Retorna una lista reactiva de deseos que se actualiza automáticamente al realizar cambios en la base de datos.

### Paso 4: Actualización de un registro (Update) 🔄

Definimos un método que actualiza los datos de un registro existente. La anotación `@Update` se encarga de esta funcionalidad.

```kotlin
@Update
abstract suspend fun updateAWish(wishEntity: Wish)
```

### Paso 5: Eliminación de un registro (Delete) ❌

Para eliminar un registro, utilizamos la anotación `@Delete`. Este método requiere como parámetro el objeto a eliminar.

```kotlin
@Delete
abstract suspend fun deleteAWish(wishEntity: Wish)
```

### Paso 6: Lectura de un registro específico por ID (Read by ID) 🔍

Usamos una consulta SQL para cargar un registro específico según su identificador.

```kotlin
@Query("Select * from `wish-table` where id=:id")
abstract fun getAWishById(id: Long): Flow<Wish>
```

- **:id**: Indica un parámetro de la consulta que se reemplaza en tiempo de ejecución.
- **Flow**: Retorna el registro como un flujo reactivo.

---

## Observaciones adicionales 📌

1. **Uso de `Flow`**: La utilización de `Flow` en lugar de `LiveData` permite una mejor integración con las corrutinas de Kotlin para manejar datos asíncronos de manera eficiente.
2. **Métodos suspendidos**: Los métodos de inserción, actualización y eliminación se marcan como `suspend` para ejecutarse fuera del hilo principal, mejorando el rendimiento.
3. **Filtros en consultas**: Las consultas pueden incluir filtros como `WHERE` para operaciones más específicas.


## Definición de la Clase de Base de Datos 📖

La base de datos se define como una clase abstracta que extiende de `RoomDatabase` y se anota con `@Database`. A continuación, se detalla el proceso.
- Definiremos una clase llamada `WishDatabase` dentro del paquete `data`. Con el siguiente código:

```kotlin
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [Wish::class],
    version = 1,
    exportSchema = false
)
abstract class WishDatabase : RoomDatabase() {
    abstract fun wishDao(): WishDao
}
```


### Descripción de los Elementos:

1. **Anotación `@Database`**:
    - **`entities`**: Lista de clases que representan tablas. En este caso, es `Wish`.
    - **`version`**: Número de versión de la base de datos. Aquí comenzamos con la versión `1`.
    - **`exportSchema`**: Se establece como `false` para evitar la exportación del esquema durante el desarrollo.

2. **Clase Abstracta `WishDatabase`**:
    - Hereda de `RoomDatabase`.
    - Contiene una función abstracta `wishDao()` que permite acceder al DAO.

## 📚 Implementación del Wish Repository

El repositorio es una capa de abstracción que maneja las operaciones de datos (`@Dao`) en nuestra aplicación. Proporciona una API limpia para acceder a los datos, asegurando la separación de preocupaciones entre las diferentes capas de la arquitectura.

## 🛠️ Creación de la clase `WishRepository`

Primero, definimos la clase `WishRepository` dentro del paquete `data`, que requerirá una instancia de `WishDao` para interactuar con la base de datos Room:

```kotlin
class WishRepository(private val wishDao: WishDao) {

    suspend fun addAWish(wish:Wish){
        wishDao.addAWish(wish)
    }

    fun getWishes(): Flow<List<Wish>> = wishDao.getAllWishes()

    fun getAWishById(id:Long) :Flow<Wish> {
        return wishDao.getAWishById(id)
    }

    suspend fun updateAWish(wish:Wish){
        wishDao.updateAWish(wish)
    }

    suspend fun deleteAWish(wish: Wish){
        wishDao.deleteAWish(wish)
    }
}
```

### 📌 Funciones principales

1. **`addAWish(wish: Wish)`**:
   - Inserta un objeto `Wish` en la base de datos de manera asíncrona usando `suspend`.

   ```kotlin
   suspend fun addAWish(wish: Wish) {
       wishDao.addAWish(wish)
   }
   ```

2. **`getWishes()`**:
   - Recupera todos los deseos como un flujo de datos (`Flow`).

   ```kotlin
   fun getWishes(): Flow<List<Wish>> = wishDao.getAllWishes()
   ```

3. **`getAWishById(id: Long)`**:
   - Recupera un deseo específico identificado por su ID como un flujo de datos.

   ```kotlin
   fun getAWishById(id: Long): Flow<Wish> {
       return wishDao.getAWishById(id)
   }
   ```

4. **`updateAWish(wish: Wish)`**:
   - Actualiza un objeto `Wish` de manera asíncrona.

   ```kotlin
   suspend fun updateAWish(wish: Wish) {
       wishDao.updateAWish(wish)
   }
   ```

5. **`deleteAWish(wish: Wish)`**:
   - Elimina un objeto `Wish` de la base de datos de manera asíncrona.

   ```kotlin
   suspend fun deleteAWish(wish: Wish) {
       wishDao.deleteAWish(wish)
   }
   ```

## ✨ Ventajas del patrón Repository

- **Separación de tareas**:
  - El `ViewModel` no necesita preocuparse por el origen de los datos (ya sea una base de datos Room, una API, etc.).
- **Facilidad de mantenimiento**:
  - Si en el futuro los datos provienen de múltiples fuentes (por ejemplo, una base de datos en la nube), solo será necesario ajustar el repositorio, sin cambios en el `ViewModel` ni en la interfaz de usuario.
- **Arquitectura recomendada**:
  - Este patrón es una de las mejores prácticas para aplicaciones Android al trabajar con bases de datos Room.


## Configuración del ViewModel

En esta sección, se detalla cómo vamos a adaptar el `ViewModel` para gestionar los datos de nuestra aplicación. El `WishViewModel` es el responsable de cargar, almacenar y actualizar los datos de las "wishes".

### Declaración del WishRepository

El `WishViewModel` requiere un repositorio de datos (`WishRepository`) como dependencia para funcionar correctamente. Este repositorio se puede inicializar de manera predeterminada utilizando una instancia predefinida.

El código se nos quedaría de la siguiente manera.

```kotlin
class WishViewModel(
    private val wishRepository: WishRepository
):ViewModel() {

   // esto ya lo teníamos antes
    var wishTitleState by mutableStateOf("")
    var wishDescriptionState by mutableStateOf("")


    fun onWishTitleChanged(newString:String){
        wishTitleState = newString
    }

    fun onWishDescriptionChanged(newString: String){
        wishDescriptionState = newString
    }

   // Nuevas funciones
    lateinit var getAllWishes: Flow<List<Wish>>

    init {
        viewModelScope.launch {
            getAllWishes = wishRepository.getWishes()
        }
    }

    fun addWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addAWish(wish= wish)
        }
    }

    fun getAWishById(id:Long):Flow<Wish> {
        return wishRepository.getAWishById(id)
    }

    fun updateWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAWish(wish= wish)
        }
    }

    fun deleteWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wish= wish)
            getAllWishes = wishRepository.getWishes()
        }
    }
}
```

## Parámetros que recibe el ViewModel

Hemos añadido un parámetro al constructor del `WishViewModel` que es el `WishRepository`. Este parámetro es necesario para que el `ViewModel` pueda interactuar con la base de datos a través del repositorio. Todavía no está completo, añadiremos algo más adelante para usar una clase llamada `Graph`


## Uso de Lateinit para Variables Inicializadas Tardíamente 🍊

En Kotlin, las variables marcadas con `lateinit` son promesas al compilador de que serán inicializadas antes de usarse. Esto es útil cuando las variables necesitan ser inicializadas de manera asíncrona, como es el caso de `Flow`. El uso de las variables `lateinit` garantiza que no se acceda a ellas antes de que estén listas y están explicadas en el manual inicial de Kotlin de este curso

### Ejemplo de Declaración Lateinit

```kotlin
lateinit var getAllWishes: Flow<List<Wish>>
```

La variable `getAllWishes` será inicializada dentro de un bloque `init` utilizando el alcance del ViewModel y una corrutina.

### Inicialización en el Bloque init

```kotlin
init {
    viewModelScope.launch {
        getAllWishes = wishRepository.getWishes()
    }
}
```

Este patrón asegura que `getAllWishes` contenga los datos necesarios antes de ser accedida en otras partes del código. Esto se hace de manera asíncrona para no bloquear el hilo principal y al inicio de la creación del ViewModel. De tal manera que cuando se necesite la lista de deseos, ya esté disponible.

## Operaciones CRUD con Corrutinas y Dispatchers 🚀

### Agregar una Wish

Para agregar una nueva "wish" al repositorio, se utiliza la función `addWish`. Esta operación se realiza en un hilo optimizado para tareas de entrada/salida (IO):

```kotlin
fun addWish(wish: Wish) {
    viewModelScope.launch(Dispatchers.IO) {
        wishRepository.addAWish(wish = wish)
    }
}
```

El uso de `Dispatchers.IO` asegura que las operaciones que pueden bloquear el hilo no afecten el rendimiento de la aplicación. Usar `Dispatchers.IO` es optativo pero es una práctica recomendada para operaciones de entrada/salida ya que no bloquea el hilo principal y hace más eficiente la aplicación.

### Obtener una Wish por ID

Esta función devuelve un `Flow` que emite una "wish" específica identificada por su ID:

```kotlin
fun getAWishById(id: Long): Flow<Wish> {
    return wishRepository.getAWishById(id)
}
```

### Actualizar una Wish

La actualización de una "wish" también se realiza en el contexto de IO:

```kotlin
fun updateWish(wish: Wish) {
    viewModelScope.launch(Dispatchers.IO) {
        wishRepository.updateAWish(wish = wish)
    }
}
```

### Eliminar una Wish

Además de eliminar una "wish", la lista de deseos se actualiza tras la operación:

```kotlin
fun deleteWish(wish: Wish) {
    viewModelScope.launch(Dispatchers.IO) {
        wishRepository.deleteAWish(wish = wish)
        getAllWishes = wishRepository.getWishes()
    }
}
```

## Dispatchers en Kotlin: Gestión de Recursos

El uso de `Dispatchers.IO` optimiza las tareas relacionadas con operaciones de entrada/salida, como leer o escribir en bases de datos o realizar llamadas a la red. Esto garantiza que el hilo principal no se bloquee, mejorando así la experiencia del usuario.


## 🛠 Resumen y recapitulación de lo que hemos hecho hasta ahora.


### 📋 Clase de Datos
La base de datos requiere una clase de datos que represente la información a almacenar. En este caso, la clase `Wish` tiene tres columnas:

1. **ID**: Clave primaria, generada automáticamente.
2. **Título**: Descripción del deseo.
3. **Descripción**: Detalles adicionales del deseo.

### 🔄 Clase DAO (Data Access Object)
El `WishDao` define las operaciones CRUD para la entidad `Wish`. 

### 🏗 Configuración de la Base de Datos
La clase `WishDatabase` extiende `RoomDatabase` e incluye las entidades y DAOs.


## 🗂 Repositorio
El repositorio sirve como intermediario entre el DAO y la capa ViewModel, manejando las funciones en un contexto adecuado, como corrutinas.

## 🎛 Configuración del ViewModel
El ViewModel utiliza el repositorio para gestionar datos y asegurar que las operaciones sucedan en el hilo adecuado.

Para finalizar nos hace falta declarar un objeto llamado `Graph` que centralice la inicialización de la base de datos y el repositorio.

## 🗺 Implementación del Objeto Graph
Vamos a crear un objeto (OJO!! no es una clase ni un fichero, será un objeto) llamado `Graph.kt` centraliza la inicialización de la base de datos y el repositorio, usando `by lazy` para una inicialización eficiente:

```kotlin
object Graph {
    lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context,
            WishDatabase::class.java,
            "wishlist.db"
        ).build()
    }
}
```

### 🧵 Inicialización Diferida con `by lazy`
El uso de `by lazy` asegura que las propiedades se inicialicen solo cuando se necesiten, optimizando el rendimiento de la aplicación.

```kotlin
val wishRepository by lazy {
    WishRepository(wishDao = database.wishDao())
}
```

### 🌍 Contexto en la Configuración
La función `provide` inicializa la base de datos mediante `Room.databaseBuilder`, especificando el contexto, la clase de la base de datos y el nombre:

```kotlin
fun provide(context: Context) {
    database = Room.databaseBuilder(
        context,
        WishDatabase::class.java,
        "wishlist.db"
    ).build()
}
```

Dónde "wishlist.db" es el nombre del archivo de la base de datos que se creará en el dispositivo.

## 📦 Introducción a la `Dependency Injection`

La inyección de dependencias (`Dependency Injection`) es un concepto fundamental en el desarrollo de aplicaciones modernas. Ayuda a que las diferentes partes de tu aplicación trabajen juntas de manera más eficiente y organizada. En esta sección, explicaremos qué es la inyección de dependencias usando una analogía sencilla.

### 🎲 Analogía: Las Reglas de un Juego de Mesa

Imagina que estás jugando a un juego de mesa. Para jugar, necesitas conocer las reglas y tener acceso a ciertos elementos, como los dados. En lugar de buscar los dados cada vez que los necesitas, hay un ayudante que te los entrega en tu turno. Esto asegura que siempre tengas lo que necesitas para jugar sin perder tiempo buscando.

En el contexto de una aplicación, la inyección de dependencias actúa como ese ayudante:

- **Proporciona** las herramientas o información que las piezas de tu aplicación necesitan para funcionar.
- **Simplifica** los cambios: si algo en tu app necesita modificarse, no tienes que buscar manualmente todas las partes afectadas; el ayudante se encarga de gestionar ese cambio.

### 🤖 Ventajas de la Inyección de Dependencias

1. **Organización**: Mantiene el código limpio y bien estructurado.
2. **Flexibilidad**: Facilita los cambios en las dependencias sin afectar a toda la aplicación.
3. **Facilidad para probar**: Permite realizar pruebas más efectivas al poder sustituir fácilmente las dependencias reales por simuladas (mock).

## 🕵️ Contexto `object Graph`

En este ejemplo, implementamos un `object Graph` utilizando el patrón de inyección de dependencias. Este patrón es útil para centralizar la gestión de dependencias en nuestra aplicación. El `object Graph` se define como un singleton llamado `Graph`. Simplemente por crearlo `object` garantiza que solo exista una instancia de este gráfico en toda la aplicación. [Más sobre el patrón Singleton](https://es.wikipedia.org/wiki/Singleton)

Vamos a volver a mirar el código del `object Graph` para entenderlo mejor.

```kotlin
object Graph {
    lateinit var database: WishDatabase

    val wishRepository by lazy{
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db").build()
    }
}
```

### 🔐 Declaración del Singleton
El objeto `Graph` se declara con la palabra clave `object` en Kotlin. Esto garantiza que sólo exista una instancia de este gráfico en toda la aplicación.

- **Ventajas del Singleton**:
  - Simplifica la gestión de dependencias.
  - Evita la creación de múltiples instancias innecesarias.

### ⚖️ Uso de `lateinit`

La propiedad `database` se define como `lateinit`, lo que significa que será inicializada más adelante, antes de ser utilizada.

- **Nota importante**:
  - Solo puede usarse con tipos no nulos.
  - En este caso, `database` es de tipo `WishDatabase`, parte de la biblioteca Room.

### ⚡ Inicialización Perezosa con `by lazy`

La propiedad `wishRepository` utiliza `by lazy`, lo que asegura que la inicialización de `WishRepository` ocurra solo la primera vez que sea accedida.

- **Beneficios de `by lazy`**:
  - Reduce el consumo de recursos.
  - Inicializa dependencias sólo cuando son necesarias.

### 🚿 Método `provide`

El método `provide` inicializa la base de datos usando `Room.databaseBuilder`.

```kotlin
fun provide(context: Context){
    database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db").build()
}
```

- **Parámetros**:
  - `context`: Proporciona el contexto necesario para inicializar la base de datos.
- **Descripción del flujo**:
  1. Crea una instancia de `WishDatabase`.
  2. Asigna esta instancia a la propiedad `database`.

## 🎭 Analogía Simplificada

Imagina que estás configurando un puesto de limonada (tu aplicación). Antes de comenzar, necesitas ciertos elementos:

- **`lateinit var database`**: Es como saber que necesitas una caja registradora para almacenar las ganancias, pero todavía no la tienes.
- **`wishRepository by lazy`**: Es como tener un libro de recetas que aparece la primera vez que decides hacer limonada. Una vez que aparece, está disponible siempre.
- **Método `provide`**: Es como decirle a tu puesto de limonada dónde está ubicado (en una feria o un vecindario). Este contexto le permite configurarse correctamente.

En términos de código, este gráfico de objetos actúa como el equipo detrás de escena que asegura que todo esté listo para que tu aplicación funcione correctamente.



# Configuración de la Clase de Aplicación 🌐
En Android, la clase de aplicación (“application class”) es útil para inicializar cualquier estado global que necesite estar disponible en toda la aplicación. Esta clase se instancia antes que cualquier otra clase cuando se crea el proceso de la aplicación.

Para usar esta clase, necesitamos crear una subclase que extienda de `Application`. Este es un paso clave para inicializar cualquier dependencia global, como la base de datos.

### Creación de la clase `WishListApp`
Primero, vamos a crear un clase Kotlin llamado `WishListApp.kt`. Este archivo contendrá nuestra clase de aplicación, que extenderá de la clase `Application`. La implementación es la siguiente:

```kotlin
import android.app.Application

class WishListApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}
```

#### Detalles clave:
- La clase `WishListApp` extiende de `Application`.
- Se sobrescribe el método `onCreate`.
- Dentro de `onCreate`, se llama a `Graph.provide(this)` para inicializar el contexto necesario para la base de datos.

### Registro de la clase en el archivo `AndroidManifest.xml`
Para asegurarnos de que la clase de aplicación sea reconocida por el proyecto, debemos registrarla en el archivo `AndroidManifest.xml`. Esto se realiza dentro de la etiqueta `<application>` mediante el atributo `android:name`.

Así es como debería verse:

```xml
<application
    android:name=".WishListApp"
    ... >
    <!-- Otros atributos y configuraciones anteriores-->
</application>
```

## 🛠️ Arreglando el Funcionamiento de la Aplicación

Si intentas ejecutar la aplicación ahora, vamos a tener un error y no se va a ejecutar. Vamos a corrigir ese problema que estaba impidiendo que nuestra aplicación funcionara correctamente. Actualmente, al ejecutar la aplicación, esta no funcionaba debido a un problema con la configuración del **ViewModel**.

### Contexto

En el **ViewModel**, se necesita un repositorio para gestionar los deseos (**wish repository**). Sin embargo, este no se estaba inicializando correctamente, lo que ocasionaba que la aplicación fallara.

### Solución

Para solucionar este problema, se configuró un valor por defecto para el repositorio, asignándolo al repositorio definido en nuestro grafo de dependencias (**Graph.wishRepository**). Esto asegura que el **ViewModel** utilice la instancia adecuada del repositorio.

#### Código del ViewModel

A continuación, se muestra la configuración del **ViewModel** después de incluir el valor por defecto:

```kotlin
class WishViewModel(
    private val wishRepository: WishRepository = Graph.wishRepository
): ViewModel() {

// Resto del código del ViewModel
}
```

### Resultado

Después de realizar esta corrección, la aplicación vuelve a ejecutarse correctamente, aunque todavía no se están cargando datos desde la base de datos hacia la interfaz de usuario (UI). Este ajuste permite que el **ViewModel** realice las operaciones necesarias, como:

- Añadir deseos.
- Obtener deseos.
- Actualizar deseos.
- Eliminar deseos.

De este modo, la base para manejar el repositorio de deseos queda lista para los siguientes pasos, donde se comenzará a interactuar con la base de datos.

## 📋 Modificación de `AddEditDetailView` para Almacenar Datos usando SnackBars y Navegación

En esta sección, vamos a modificar la pantalla `AddEditDetailView` para permitir la creación de un deseo nuevo. De momento solo vamos a centrarnos en la vista sin la lógica de la base de datos.

Primero, configuramos los elementos necesarios para manejar la interfaz y la lógica:

### 1. **Mensaje SnackBar**
Creamos un mensaje **SnackBar** que aparecerá en la parte inferior de la pantalla al agregar un deseo:

```kotlin
val snackMessage = remember {
    mutableStateOf("")
}
```

### 2. **Scope para operaciones asíncronas**
Declaramos un `scope` utilizando una **coroutine** para ejecutar métodos asíncronos, como almacenar datos en la base de datos:

```kotlin
val scope = rememberCoroutineScope()
```

### 3. **ScaffoldState**
El `scaffoldState` se encarga de gestionar el estado del **Scaffold**, incluyendo el **SnackBar**:

```kotlin
val scaffoldState = rememberScaffoldState()
```

---

## 🏗 Estructura del Scaffold

Vamos a añadir un `scaffoldState` en nuestro Scaffold para gestionar el estado del **SnackBar**.

```kotlin
Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
        AppBarView(
            title = if (id != 0L) "Actualizar Deseo" else "Agregar Deseo"
        ) { navController.navigateUp() }
    },
) { ... }
```

### 1. **Botón: Lógica de Crear o Actualizar Deseo**

El botón **Add/Update** maneja la lógica para agregar o actualizar un deseo, de momento vamos a centrarnos en la lógica de agregar un deseo. Y en lanzar un mensaje de error usando el **SnackBar** si no se rellenan los campos.

```kotlin
Button(onClick = {
    if (viewModel.wishTitleState.isNotEmpty() &&
        viewModel.wishDescriptionState.isNotEmpty()
    ) {
        if (id != 0L) {
            // TODO: Actualizar un deseo existente
        } else {
            // Crear un nuevo deseo
            viewModel.addWish(
                Wish(
                    title = viewModel.wishTitleState.trim(),
                    description = viewModel.wishDescriptionState.trim()
                )
            )
            snackMessage.value = "Se ha creado el deseo"
        }
    } else {
        snackMessage.value = "Debes rellenar los valores de un deseo para poder crearlo"
    }

    // Mostrar SnackBar y navegar hacia atrás
    scope.launch {
        scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
        navController.navigateUp()
    }
})
```

---

## 🍬 Visualización del SnackBar

Para mostrar un **SnackBar**, utilizamos el `scaffoldState` con su **snackbarHostState**:

```kotlin
scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
    navController.navigateUp()
}
```

La primera línea (`scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)`) lo que nos permite es mostrar el mensaje del **SnackBar** que dura unos segundos, y cuando termine de mostrarse, navegamos hacia atrás.

Prueba a ejecutarlo con esa línea de código y prueba comentando dicha línea de código.


## 📋 Mostrar los elementos de la lista de deseos desde la base de datos

En esta sección, aprenderemos cómo mostrar los elementos de la lista de deseos almacenados en nuestra base de datos. Hasta ahora hemos estado usando datos ficticios (dummy data) en la **HomeView**. Ahora reemplazaremos esos datos por los datos reales obtenidos de nuestra base de datos.

### 1. Modificar la **HomeView**
La Home View es donde estamos mostrando actualmente la lista de deseos mediante una `LazyColumn`.

### 2. Crear una variable para cargar los datos
Primero, necesitamos cargar los datos desde nuestra base de datos. Para ello, usaremos la función del **ViewModel** que obtiene la lista de deseos y la recopilaremos como estado.

### Código:

```kotlin
// Dentro de la Home View antes de definir la LazyColumn
val wishList = viewModel.getAllWishes.collectAsState(initial = listOf())
```

- `viewModel.getAllWishes()` obtiene la lista de deseos desde el **ViewModel**.
- `collectAsState` recopila los datos como un estado que puede observarse.
- `initial = emptyList()` asegura que el estado inicial sea una lista vacía hasta que los datos se carguen.

### 3. Usar los datos en la **LazyColumn**
Una vez que los datos han sido recopilados, podemos reemplazar la lista ficticia de valores dummy por la lista real obtenida desde la base de datos.

### Código:

```kotlin
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Contenido de la lista de la BBDD
            items(wishList.value){
                wish -> WishItem(wish = wish){

            }
            }
        }
```

### 📌 Detalles importantes:
- `wishList.value` contiene los datos cargados desde la base de datos.
- `items()` itera sobre cada elemento en la lista y nos permite acceder a cada **wish**.

Si ejecutas ahora la aplicación, te darás cuenta que se muestran todos los datos que tenemos en la BBDD, y que seguramente aparezcan los datos que hayas metido anteriormente de forma aleatoria para probar la app.

## 📱 Navegación y Edición de Deseos

En esta sección, implementaremos la funcionalidad que permite navegar desde un elemento de la lista de deseos hasta una pantalla de edición, donde podremos cargar los datos del deseo seleccionado y realizar modificaciones.

## 🔗 Navegación desde la Lista de Deseos

Cuando un usuario hace clic en un elemento de la lista de deseos, el sistema debe navegar a la pantalla de edición, pasando el identificador del deseo seleccionado. En la **HomeView**, configuramos la acción del elemento:

```kotlin
items(wishList.value) { wish ->
    WishItem(wish = wish) {
        val id = wish.id
        navController.navigate(Screen.AddScreen.route + "/$id")
    }
}
```
### Explicación Detallada

1. **items(wishList.value)**: Utiliza el método `items` de un `LazyColumn` para recorrer una lista observable (`wishList.value`) que viene de la BBDD

2. **WishItem(wish = wish)**: 
   - `WishItem` es un composable que recibe el objeto `wish` como parámetro.
   - El segundo bloque de llaves **{ ... }** es una lambda de acción que se ejecuta cuando se interactúa (como al hacer clic en el evento `onClick` de `WishItem`).

3. **val id = wish.id**:
   - Extrae el ID del elemento `wish` para poder hacer la navegación en la siguiente instrucción

4. **navController.navigate(...)**:
   - Utiliza `navController` para navegar a una nueva ruta.
   - La ruta se forma concatenando el nombre de la pantalla `Screen.AddScreen.route` con el `id` del elemento seleccionado.

   Ejemplo de ruta:
   ```
   Screen.AddScreen.route + "/42"   // Navega a AddScreen con ID 42
   ```


## 🗺️ Configuración de la Navegación

Segundo, debemos configurar la ruta de navegación y eso lo hacemos en la función `Navigation`, configuramos el **NavHost** para incluir una ruta que permite pasar el identificador (**ID**) del deseo seleccionado a la pantalla de edición:

```kotlin
@Composable
fun Navigation(
    viewModel: WishViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(navController, viewModel)
        }
        composable(Screen.AddScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )) { entry ->
            val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            AddEditDetailView(
                id = id,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
```

### 🔍 **Parte 1: Definición del Argumento**
La sección clave donde se define el argumento **id**:

```kotlin
arguments = listOf(
    navArgument("id") {
        type = NavType.LongType
        defaultValue = 0L
        nullable = false
    }
)
```

#### ▶ **Explicación:**
1. **`navArgument("id")`**: Define un argumento llamado `id` que será parte de la ruta de navegación.
2. **`type = NavType.LongType`**: Especifica que el tipo de dato del argumento `id` es `Long` (número entero largo).
3. **`defaultValue = 0L`**: Define un valor por defecto `0L` en caso de que no se proporcione el argumento.
4. **`nullable = false`**: Especifica que el argumento `id` **no puede ser nulo**.

De esta manera, la ruta queda configurada para aceptar un argumento de tipo `Long`, asegurando un manejo seguro de valores por defecto.


### 🔎 **Parte 2: Recuperación del Argumento**
La sección donde se obtiene el valor del argumento `id`:

```kotlin
val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else 0L
```

#### ▶ **Explicación:**
1. **`entry.arguments`**: Representa los argumentos pasados a la composable.
2. **`entry.arguments!!.getLong("id")`**:
   - Utiliza **`getLong("id")`** para recuperar el argumento `id` como un valor de tipo `Long`.
   - El operador `!!` asegura que el valor no sea nulo (aunque en este caso ya se define como no nulo).
3. **`else 0L`**: Si `entry.arguments` es `null` (por alguna razón inesperada), se asigna el valor por defecto `0L`.

Esta línea garantiza que el valor `id` se recupere de forma segura, incluso si algo sale mal.

### Cambio en la ruta de navegación del FAB.

En la HomeView tenemos un FAB y cuando es pulsado tenemos un evento onClick que nos lleva a la pantalla de edición de deseos.
Ahora la ruta de navegación a la pantalla de edición de deseos se ha modificado para incluir el ID del deseo seleccionado.
Por lo tanto, debemos actualizar la ruta de navegación en el evento onClick del FAB, de lo contrario la aplicación "crashseará" 💣

### Código Actualizado:

```kotlin
onClick = {
            navController.navigate(Screen.AddScreen.route + "/0L")
          }
```

Le estamo añadiendo simplemente que navegue a la ruta pasando un 0L, que es el valor por defecto que hemos definido en la ruta de navegación para crear un deseo nuevo.

Ahora podrías probar la aplicación, pero te darás cuenta que al pinchar un deseo, se navega a la pantalla de edición pero no se cargan los datos del deseo seleccionado. Eso se debe a que no hemos implementado la lógica para cargar los datos del deseo seleccionado en la pantalla de edición. 

## 📦 Cargar Datos del Deseo Seleccionado (UpdateAWWish)
En la pantalla de edición de deseos, necesitamos cargar los datos del deseo seleccionado para permitir su modificación. Eso significa que después de la definición de las variables debemos cargar los datos del deseo seleccionado si es distinto de 0L.


```kotlin  
    val snackMessage = remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (id != 0L){
        val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L, "", ""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    }else{
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }
```

## Explicación Paso a Paso

### 1. **Condición Inicial**

```kotlin
if (id != 0L) {
```
- La condición verifica si `id` no es igual a **0L**. Si es 0L se considera que es añadir un deseo y lo contario sería recuperar (actualizar) un deseo existente.
- `0L` representa el valor **0** en tipo **Long**.
- Si `id` es distinto de 0, se considera que el identificador es válido y se procede a obtener el objeto **Wish**.

### 2. **Recuperación de Datos con `collectAsState`**

```kotlin
val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L, "", ""))
```
- **`getAWishById(id)`**: Es una función en el `ViewModel` que recupera un flujo de datos (
`Flow`) correspondiente al identificador `id`.
- **`collectAsState`**: Se usa para **convertir un `Flow` en un estado reactivo** que puede ser observado y gestionado por la interfaz de usuario de Jetpack Compose.
- **`initial = Wish(0L, "", "")`**: Define un valor inicial por defecto de tipo `Wish`. Este objeto tiene:
  - `id`: 0L (sin valor válido).
  - `title`: Cadena vacía.
  - `description`: Cadena vacía.

### 3. **Asignación de Estado**

```kotlin
viewModel.wishTitleState = wish.value.title
viewModel.wishDescriptionState = wish.value.description
```
- **`wish.value`**: Se accede al valor actual del estado recuperado desde el `Flow`.
- **`viewModel.wishTitleState` y `viewModel.wishDescriptionState`**: Se actualizan con los valores de `title` y `description` del objeto `Wish` recuperado.

### 4. **Condición `else`**

```kotlin
} else {
    viewModel.wishTitleState = ""
    viewModel.wishDescriptionState = ""
}
```
- Si el `id` es **0L**, se consideran estados vacíos.
- Las variables `wishTitleState` y `wishDescriptionState` se establecen como cadenas vacías (**""**).

Pero OJO! Ahora se cargan los datos, pero si aprietas al botón "Actualizar" verás que no se cambian los valores. Eso se debe a que todavía no hemos actualizado la parte del `AddEditDetailView` donde tenemos que actualizar los datos. Recuerda, teníamos marcado una línea con: `// TODO: Actualizar un deseo existente`

## 🔄 Actualizar el Wish cuando se pulsa el botón `Actualizar`
Recuerdas que en `AddEditDetailView` teníamos que programar qué hacer cuando nos han pulsado el botón de actualizar deseo más concretamente lo teníamos marcado con `// TODO: Actualizar un deseo existente`

Lo sustituimos por el siguiente código:

```kotlin
viewModel.updateWish(
    Wish(
        id = id,
        title = viewModel.wishTitleState.trim(),
        description = viewModel.wishDescriptionState.trim()
    )
)
```

### 📌 **Explicación Paso a Paso:*

El código realiza una **actualización** de un objeto `Wish` utilizando el **ViewModel**. El objetivo es actualizar un deseo existente en la aplicación mediante los siguientes pasos:

1. **Creación de un nuevo objeto `Wish`**:
   - Se asigna un identificador `id`.
   - Se utiliza `viewModel.wishTitleState.trim()` para obtener el título del deseo, eliminando espacios en blanco al inicio y al final (eso lo hace trim()).
   - Se usa `viewModel.wishDescriptionState.trim()` para obtener la descripción del deseo, igualmente limpiando los espacios innecesarios.

2. **Llamada al método `updateWish`** del `ViewModel`:
   - Se envía el objeto `Wish` actualizado para que el ViewModel se encargue de procesar y actualizar los datos.

Si ahora pruebas la aplicación verás que puedes actualizar los datos de un deseo existente. Podemos crear y actualizar deseos, pero todavía no podemos eliminar. Vamos a ver cómo hacerlo.

## 🗑 Implementando un `Swipe to Delete`

Para poder implementar la funcionalidad de `Swipe to Delete`, en nuestra función `HomeView` vamos a añadir varios funcionalidades dentro de la `LazyColumn` cuando lo llenamos con los datos. Concretamente, donde tenemos esté código vamos a añadir la funcionalidad de `Swipe to Delete`.

```kotlin	
items(wishList.value) { wish ->
    WishItem(wish = wish) {
        val id = wish.id
        navController.navigate(Screen.AddScreen.route + "/$id")
    }
}
```

Y lo sustituiremos por el siguiente código:

```kotlin
items(wishList.value) { wish ->
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                viewModel.deleteWish(wish)
            }
            true // Confirmación de que el cambio de estado es aceptado
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = { // TODO: Lo implementaremos más adelante
        },
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.25f) },
        dismissContent = {
            WishItem(wish = wish) {
                val id = wish.id
                navController.navigate(Screen.AddScreen.route + "/$id")
            }
        }
    )
}
```
OJO! es posible que FractionalThreshold(0.25f) aparezca como deprecado mostrandose como tachado. 


El código está compuesto por:

- **rememberDismissState**: Maneja el estado del deslizamiento y confirma la eliminación del elemento.
- **SwipeToDismiss**: Componente de Compose que envuelve cada elemento y agrega funcionalidad de deslizamiento.

#### **1. rememberDismissState**

- `rememberDismissState` almacena el estado del deslizamiento y reacciona a los cambios.
- La lógica de confirmación (“confirmStateChange”) comprueba si el elemento ha sido deslizado completamente a la derecha (**DismissedToEnd**) o izquierda (**DismissedToStart**).
- Si la acción ocurre, se invoca `viewModel.deleteWish(wish)` para eliminar el elemento.
- La función confirmStateChange devuelve un valor booleano que le indica a rememberDismissState si debe continuar con la acción de cambio de estado.
  - `true`: El cambio de estado se acepta y se procede con el efecto de deslizamiento (como eliminar el ítem).
  - `false`: El cambio de estado se cancela y el elemento regresa a su posición original.

#### **2. SwipeToDismiss**

`SwipeToDismiss` define:
- **state**: El estado del deslizamiento manejado por `dismissState`.
- **background**: Una composición (pendiente de implementación) que se mostrará detrás del elemento al deslizarlo.
- **directions**: Define la dirección del deslizamiento permitido. En este caso, `DismissDirection.EndToStart` (de derecha a izquierda). Podemos definir `DismissDirection.StartToEnd` para permitir el deslizamiento en la dirección opuesta (de izquierda a derecha) o ambas direcciones, date cuenta de que es un conjunto de direcciones.
- **dismissThresholds**: Define el umbral para confirmar el deslizamiento. Se usa `FractionalThreshold(0.25f)` para que el elemento solo se elimine al deslizarlo un 25% de su ancho. Si hubieramos puesto 1f, se eliminaría al deslizarlo completamente.
- **dismissContent**: El contenido visible del elemento, que en este caso es `WishItem`.


Si pruebas tu código ahora verás que puedes eliminar los deseos deslizando hacia la izquierda. Pero ocurre algo muy extraño, parece que aparezcan huecos en medio de la lista de deseos. Vamos a ver cómo solucionar este problema. Bien, lo único que tenemos que hacer es añadir una pequeña línea y donde teníamos esto:

```kotlin
items(wishList.value) { wish ->
```

Lo sustituimos por esto:

```kotlin
items(wishList.value, key = { wish -> wish.id }) { wish ->
```

La clave **`key = { wish -> wish.id }`** en el bloque `items` es fundamental para que el estado de cada elemento persista correctamente cuando trabajas con listas dinámicas. Si **no defines una clave única**, Compose asume que los elementos están en el mismo orden, y al eliminar o mover un elemento, los estados pueden confundirse, causando *stacking incorrecto* o comportamientos inesperados.

La clave **`key = { wish -> wish.id }`** asocia de forma única cada elemento a su **id**, lo que garantiza:

1. **Persistencia de estado**: Cada elemento mantiene su estado (como el swipe) incluso si otros se eliminan o reordenan.
2. **Optimización de Compose**: Los elementos no cambiados se reutilizan, mejorando el rendimiento.
3. **Deslizamiento correcto**: Al eliminar un elemento mediante swipe, los demás no pierden su estado ni presentan animaciones incorrectas.

Sin una clave única:

- Compose solo se basa en la posición de los elementos (índices).
- Al eliminar un elemento, los índices se recalculan y el estado puede aplicarse al elemento incorrecto.
- Esto causa "saltos" o estados equivocados en los elementos restantes.

| Con `key = { wish -> wish.id }`                         | Sin `key`                                     |
|---------------------------------------------------------|----------------------------------------------|
| Cada elemento tiene una identidad única persistente.    | Los elementos se identifican por su posición. |
| El swipe afecta solo al elemento deslizado.             | El swipe puede afectar a elementos incorrectos. |
| El resto de la lista se reorganiza **correctamente**.   | Los elementos pueden saltar o apilarse mal.   |

## ⚖️ Swipe to Delete con Fondo Dinámico

Vamos a añdir un fondo dinámico al gesto de **Swipe to Delete** en nuestra aplicación. El fondo cambiará de color dependiendo de la dirección del deslizamiento.
Nuestro objetivo será crear un fondo rojo con un ícono de eliminar que se muestre al deslizar un elemento hacia la izquierda.


### 📚 Código Completo

En la función `HomeView`, actualizamos el contenido de `SwipeToDismiss`. Dejamos un comentario con el `background` pendiente de implementación. Ahora es el momento de implementarlo y sustituirlo por el siguiente código.

```kotlin
background = {
    // Color dinámico usando animateColorAsState
    val color by animateColorAsState(
        if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Red 
        else Color.Transparent,
        label = ""
    )

    // Alineación del contenido
    val alignment = Alignment.CenterEnd

    // Contenedor Box con el fondo y el ícono
    Box(
        Modifier
            .fillMaxSize() // Ocupa todo el tamaño
            .background(color) // Fondo dinámico
            .padding(horizontal = 20.dp), // Padding horizontal
        contentAlignment = alignment
    ) {
        Icon(
            Icons.Default.Delete, // Ícono de eliminar
            contentDescription = "Delete Icon", // Descripción del contenido
            tint = Color.White // Color blanco
        )
    }
},
```

### ▶️ Implementación Paso a Paso

1. **Configuramos el color dinámico** usando `animateColorAsState`. Dependiendo de la dirección del deslizamiento, el color será **rojo** o **transparente**.

2. **Alineamos el contenido** al extremo derecho utilizando `Alignment.CenterEnd`.

3. **Creamos un contenedor `Box`** con un fondo y padding adecuados para el gesto de deslizamiento.

4. **Añadimos un ícono de eliminar** (`Icons.Default.Delete`) con un color blanco.


### 🔍 Explicación Detallada

1. **`animateColorAsState`**: 
   - Animamos el color dependiendo de la dirección del deslizamiento. Si el `dismissDirection` es `EndToStart` (de derecha a izquierda), el color será **rojo** (`Color.Red`).
   - De lo contrario, el color será **transparente** (`Color.Transparent`).

2. **`Alignment.CenterEnd`**:
   - Alinea el contenido (el ícono de eliminar) al centro del extremo derecho del contenedor.

3. **`Box`**:
   - Es un contenedor versátil que permite añadir un fondo, padding y contenido alineado.
   - Usamos `Modifier.fillMaxSize()` para ocupar todo el espacio disponible.
   - El fondo se asigna dinámicamente con `background(color)`.
   - El padding horizontal se define en **20.dp**.

4. **`Icon`**:
   - Muestra el ícono de eliminar usando `Icons.Default.Delete`.
   - Se asigna una descripción accesible (“Delete Icon”).
   - El color del ícono es **blanco** (`Color.White`).


### ⚖️ Resultado Visual

1. Al deslizar un elemento hacia la izquierda:
   - Se muestra un fondo **rojo**.
   - Aparece un ícono de eliminar **blanco**.

2. Si el deslizado no supera el **umbral definido**, el elemento no se elimina.

3. El gesto **Swipe to Delete** sólo acepta la dirección de derecha a izquierda (**EndToStart**).

## Fin del laboratorio.

Si has llegado hasta aquí significa que has terminado el laboratorio: Enhorabuena! 🎉

Si te has perdido, en el siguiente apartado te dejo el código completo de la aplicación para que puedas revisarlo y compararlo con el tuyo.

# Código Completo de la Aplicación

Puedes acceder al código pinchando en el [siguiente enlace:](./code/)

O pegado al manual aquí abajo.

<details>
  <summary>Pincha para ver el código.</summary>
<br>

## `data.Wish.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="wish-table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="wish-title")
    val title: String="",
    @ColumnInfo(name="wish-desc")
    val description: String=""
)


object DummyWish {
    val wishList = listOf(
        Wish(
            title = "Batidora galáctica 3000",
            description = "Prepara smoothies que te llevarán a otra dimensión 🚀"
        ),
        Wish(
            title = "Auriculares antimolestias",
            description = "¡Apaga al mundo y enciende tu música! 🎧"
        ),
        Wish(
            title = "Silla gamer super pro",
            description = "Porque sentarse como un profesional también cuenta 🕹️"
        ),
        Wish(
            title = "Curso de Llados",
            description = "Hazte unos burpees y baja esa fucking panza 💪"
        )
    )
}
```

## `data.WishDao.kt`


```kotlin
package es.uva.inf5g.psm.listadedeseos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WishDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addAWish(wishEntity: Wish)

    // Loads all wishes from the wish table
    @Query("Select * from `wish-table`")
    abstract fun getAllWishes(): Flow<List<Wish>>

    @Update
    abstract suspend fun updateAWish(wishEntity: Wish)

    @Delete
    abstract suspend fun deleteAWish(wishEntity: Wish)

    @Query("Select * from `wish-table` where id=:id")
    abstract fun getAWishById(id:Long): Flow<Wish>


}
```

## `data.WishDatabase.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos.data

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [Wish::class],
    version = 1,
    exportSchema = false
)
abstract class WishDatabase : RoomDatabase() {
    abstract fun wishDao(): WishDao
}

```

## `data.WishRepository.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos.data

import kotlinx.coroutines.flow.Flow


class WishRepository(private val wishDao: WishDao) {

    suspend fun addAWish(wish:Wish){
        wishDao.addAWish(wish)
    }

    fun getWishes(): Flow<List<Wish>> = wishDao.getAllWishes()

    fun getAWishById(id:Long) :Flow<Wish> {
        return wishDao.getAWishById(id)
    }

    suspend fun updateAWish(wish:Wish){
        wishDao.updateAWish(wish)
    }

    suspend fun deleteAWish(wish: Wish){
        wishDao.deleteAWish(wish)
    }

}

```


## `AddEditDetailView.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import es.uva.inf5g.psm.listadedeseos.data.Wish
import kotlinx.coroutines.launch

@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavController
) {
    val snackMessage = remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (id != 0L){
        val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L, "", ""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    }else{
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(
                title =
                if (id != 0L) stringResource(id = R.string.update_wish)
                else stringResource(id = R.string.add_wish)
            ) { navController.navigateUp() }
        },

        ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            WishTextField(label = "Title",
                value = viewModel.wishTitleState,
                onValueChange = {
                    viewModel.onWishTitleChanged(it)
                })

            Spacer(modifier = Modifier.height(10.dp))

            WishTextField(label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChange = {
                    viewModel.onWishDescriptionChanged(it)
                })

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                if (viewModel.wishTitleState.isNotEmpty() &&
                    viewModel.wishDescriptionState.isNotEmpty()
                ) {
                    // Lógica para añadir o modificar un deseo.
                    if (id != 0L) {
                        viewModel.updateWish(
                            Wish(
                                id = id,
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                    } else {
                        //  Lógica para añadir un deseo
                        viewModel.addWish(
                            Wish(
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                        snackMessage.value = "Se ha creado el deseo"
                    }
                } else {
                    //Error
                    snackMessage.value = "Debes rellenar los valores de un deseo para poder crearlo"
                }
                scope.launch{
                    //scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                    navController.navigateUp()
                }


            }) {
                Text(
                    text = if (id != 0L) stringResource(id = R.string.update_wish)
                    else stringResource(
                        id = R.string.add_wish
                    ),
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }
    }

}


@Composable
fun WishTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label, color = Color.Black)
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        //Aquí puedes definir los colores del campo de texto a tu gusto
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black, // Color del texto
            cursorColor = Color.Blue, // Color del cursor
            focusedBorderColor = Color.Green, // Color del borde con foco
            unfocusedBorderColor = Color.Gray, // Color del borde sin foco
            focusedLabelColor = Color.Green, // Color del label cuando está enfocado
            unfocusedLabelColor = Color.Gray // Color del label cuando no está enfocado
        )
    )
}

@Preview(showBackground = true)
@Composable
fun WishTextFieldPreview() {
    WishTextField(
        label = "Título",
        value = "Escribe aquí el título del deseo",
        onValueChange = {}
    )
}
```

## `AppBar.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun AppBarView(
    title: String,
    onBackNavClicked: () -> Unit= {}
){
    val navigationIcon : (@Composable () -> Unit)? =
        if(!title.contains("Lista de los deseos")){
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        }else{
            null
        }

    TopAppBar(
        title = {
            Text(text = title,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp))
        },
        elevation = 3.dp,
        backgroundColor = colorResource(id = R.color.app_bar_color),
        //Aquí pasamos el icono de navegación que hemos creado arriba.
        navigationIcon = navigationIcon
    )
}
```

## `Graph.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import android.content.Context
import androidx.room.Room
import es.uva.inf5g.psm.listadedeseos.data.WishDatabase
import es.uva.inf5g.psm.listadedeseos.data.WishRepository

object Graph {
    private lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context,
            WishDatabase::class.java,
            "wishlist.db"
        ).build()
    }
}
```

## `HomeView.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissDirection
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FractionalThreshold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.uva.inf5g.psm.listadedeseos.data.Wish

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AppBarView(title = "Lista de los deseos") {
                Toast.makeText(context, "Botón pulsado", Toast.LENGTH_LONG).show()
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()), // Agregar espacio para evitar superposición
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = Color.Black,
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { it ->
        val wishList = viewModel.getAllWishes.collectAsState(initial = listOf())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Contenido de la lista
            items(wishList.value, key = { wish -> wish.id }) { wish ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                            viewModel.deleteWish(wish)
                        }
                        true
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    background = {
                        // Color dinámico usando animateColorAsState
                        val color by animateColorAsState(
                            if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Red
                            else Color.Transparent,
                            label = ""
                        )

                        // Alineación del contenido
                        val alignment = Alignment.CenterEnd

                        // Contenedor Box con el fondo y el ícono
                        Box(
                            Modifier
                                .fillMaxSize() // Ocupa todo el tamaño
                                .background(color) // Fondo dinámico
                                .padding(horizontal = 20.dp), // Padding horizontal
                            contentAlignment = alignment
                        ) {
                            Icon(
                                Icons.Default.Delete, // Ícono de eliminar
                                contentDescription = "Delete Icon", // Descripción del contenido
                                tint = Color.White // Color blanco
                            )
                        }
                    },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.25f) },
                    dismissContent = {
                        WishItem(wish = wish) {
                            val id = wish.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable { onClick() },
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = wish.title,
                fontWeight = FontWeight.ExtraBold
            )
            Text(text = wish.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishItemPreview() {
    val sampleWish = Wish(
        id = 1L,
        title = "Aprobar esta asignatura 🎓",
        description = "¡Por favor, Kotlin, haz tu magia! 🚀"
    )
    WishItem(wish = sampleWish, onClick = { /* Acción simulada */ })
}
```

## `MainActivity.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import es.uva.inf5g.psm.listadedeseos.ui.theme.ListaDeDeseosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaDeDeseosTheme {
                Navigation()
            }
        }
    }
}
```

## `Navigation.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun Navigation(
    viewModel: WishViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(navController, viewModel)
        }
        composable(Screen.AddScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )) { entry ->
            val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            AddEditDetailView(
                id = id,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}


```

## `Screen.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object AddScreen : Screen("add_screen")
}
```

## `WishlistApp.kt`

```kotlin

package es.uva.inf5g.psm.listadedeseos

import android.app.Application

class WishListApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}
```

## `WishViewModel.kt`

```kotlin
package es.uva.inf5g.psm.listadedeseos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uva.inf5g.psm.listadedeseos.data.Wish
import es.uva.inf5g.psm.listadedeseos.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishViewModel(
    private val wishRepository: WishRepository = Graph.wishRepository
):ViewModel() {

    var wishTitleState by mutableStateOf("")

    var wishDescriptionState by mutableStateOf("")

    fun onWishTitleChanged(newString: String) {
        wishTitleState = newString
    }

    fun onWishDescriptionChanged(newString: String) {
        wishDescriptionState = newString
    }

    lateinit var getAllWishes: Flow<List<Wish>>

    init {
        viewModelScope.launch {
            getAllWishes = wishRepository.getWishes()
        }
    }

    fun addWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addAWish(wish= wish)
        }
    }

    fun getAWishById(id:Long):Flow<Wish> {
        return wishRepository.getAWishById(id)
    }

    fun updateWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAWish(wish= wish)
        }
    }

    fun deleteWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAWish(wish= wish)
            getAllWishes = wishRepository.getWishes()
        }
    }
}
```
</details>
<br>
