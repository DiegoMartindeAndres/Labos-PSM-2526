## 🌿 Introducción a la Programación Funcional

Kotlin es un lenguaje **moderno, expresivo y multiparadigma**, lo que significa que combina lo mejor de la **programación orientada a objetos (POO)** con la **programación funcional (FP)**.
La programación funcional nos permite escribir código más **declarativo, conciso y seguro**.

### 💡 ¿Qué es la programación funcional?

Es un paradigma basado en **funciones puras**, **inmutabilidad** y **composición de funciones**.

A diferencia del enfoque imperativo (donde decimos *cómo* hacer algo paso a paso), en el enfoque funcional describimos *qué* queremos lograr.

```kotlin
// Estilo imperativo
val nombres = mutableListOf<String>()
for (animal in animales) {
    nombres.add(animal.nombre)
}
println(nombres)

// Estilo funcional
val nombresFuncional = animales.map { it.nombre }
println(nombresFuncional)
```

Ambos fragmentos logran lo mismo, pero el segundo se centra en **el resultado**, no en los pasos intermedios.

---

### 🔁 Principios básicos

| Concepto               | Descripción                                                                          | Ejemplo en Kotlin                        |
| ---------------------- | ------------------------------------------------------------------------------------ | ---------------------------------------- |
| **Inmutabilidad**      | No modificamos los datos originales, creamos nuevos valores.                         | `val nueva = vieja + "nuevo"`            |
| **Funciones puras**    | Siempre devuelven el mismo resultado con la misma entrada y sin efectos secundarios. | `fun cuadrado(x: Int) = x * x`           |
| **Expresiones lambda** | Pequeñas funciones anónimas que podemos pasar como argumentos.                       | `val doble = { x: Int -> x * 2 }`        |
| **Encadenamiento**     | Aplicar varias transformaciones en secuencia.                                        | `lista.filter { it > 5 }.map { it * 2 }` |

---

### ⚙️ ¿Por qué es importante?

* ✅ **Código más legible:** describe qué hacer, no cómo.
* 🔒 **Menos errores:** gracias a la inmutabilidad.
* ⚡ **Más eficiente en concurrencia:** las funciones puras son seguras en paralelo.
* 🧩 **Ideal para colecciones:** transformar, filtrar y combinar datos es natural.

---

### 🌍 ¿Dónde se usa?

La programación funcional está presente en muchos lenguajes modernos:

* **Kotlin**, **Swift**, **Scala**, **Rust** → combinan FP con POO
* **Haskell**, **Clojure** → 100% funcionales
* **JavaScript**, **Python** → integran `map()`, `filter()`, `reduce()`

En Android y Kotlin se usa constantemente para:

* Procesar colecciones (`map`, `filter`, `reduce`)
* Evitar `null` con `?.`, `let`, `run`, `apply`
* Programación reactiva con **LiveData**, **Flow**, o **Coroutines**

---

## 📦 Primero el modelo: Data Class: Animal

Primero, creamos una data class llamada `Animal`. Esta clase representará diferentes animales, cada uno con un nombre y una edad.

```kotlin
// Definimos la data class Animal
data class Animal(val nombre: String, val edad: Int, val tipo: String)

// Creamos una lista de animales
val animales = listOf(
    Animal("León", 8, "Mamífero"),
    Animal("Tortuga", 120, "Reptil"),
    Animal("Perro", 5, "Mamífero")
)
```

Ahora que tenemos la clase `Animal` definida podemos usarla de ejemplo para ilustrar los conceptos de la programación funcional en Kotlin.

---

### 🧩 Ejemplo: Imperativo vs Funcional

Queremos obtener los nombres de los animales mayores de 10 años en mayúsculas.

```kotlin
// Imperativo
val mayores = mutableListOf<String>()
for (animal in animales) {
    if (animal.edad > 10) {
        mayores.add(animal.nombre.uppercase())
    }
}
println(mayores)

// Funcional
val mayoresFuncional = animales
    .filter { it.edad > 10 }
    .map { it.nombre.uppercase() }

println(mayoresFuncional)
```

El enfoque funcional es más **conciso, limpio y expresivo**.

---

## 🧩 Funciones funcionales sobre colecciones (Iterable, List, etc.)

### 🗺️ Map

El operador `map` nos permite **transformar cada elemento de una colección** aplicando una función a cada uno de ellos.
Es muy útil para crear una nueva lista a partir de otra, transformando sus elementos.

```kotlin
// Queremos obtener una lista con los nombres de todos los animales
val nombresAnimales = animales.map { it.nombre }
println(nombresAnimales) // [León, Tortuga, Perro]
```

La función lambda `{ it.nombre }` se aplica a cada `Animal` en la lista original.

También podemos hacer transformaciones más complejas:

```kotlin
val descripciones = animales.map { "${it.nombre} tiene ${it.edad} años" }
println(descripciones)
// [León tiene 8 años, Tortuga tiene 120 años, Perro tiene 5 años]
```

### 🔍 Filter

El operador `filter` nos permite **seleccionar solo los elementos que cumplen una condición**.
Devuelve una nueva lista con los elementos que pasan el filtro, sin modificar la colección original.

---

```kotlin
// Queremos obtener solo los animales mayores de 10 años
val animalesViejos = animales.filter { it.edad > 10 }
println(animalesViejos)
// [Animal(nombre=Tortuga, edad=120)]
```

En este ejemplo, `filter` recorre la lista `animales` y mantiene únicamente aquellos cuya edad sea mayor que 10.
Es una forma declarativa y limpia de expresar condiciones.

Podemos combinarlo fácilmente con otros operadores como `map`:

```kotlin
// Nombres en mayúsculas de los animales mayores de 10 años
val nombresMayores = animales
    .filter { it.edad > 10 }
    .map { it.nombre.uppercase() }

println(nombresMayores)
// [TORTUGA]
```


## 📋 Copy

El método `copy` es útil para **crear una copia de un objeto modificando uno o varios atributos**, manteniendo la inmutabilidad.

```kotlin
// Creamos una copia del perro pero cambiamos su edad
val perroViejo = animales[2].copy(edad = 10)
println(perroViejo) // Animal(nombre=Perro, edad=10)
```

Podemos incluso crear varias copias en cadena:

```kotlin
val perroAnciano = perroViejo.copy(edad = 15)
println(perroAnciano) // Animal(nombre=Perro, edad=15)
```

### 🔁 forEach

El operador `forEach` nos permite **recorrer una colección y ejecutar una acción por cada elemento**.
A diferencia de `map` o `filter`, **no devuelve ningún resultado**, solo realiza operaciones (por ejemplo, imprimir o registrar datos).

---

```kotlin
// Mostramos por pantalla cada animal de la lista
animales.forEach {
    println("🐾 ${it.nombre} tiene ${it.edad} años")
}
```

Salida:

```
🐾 León tiene 8 años
🐾 Tortuga tiene 120 años
🐾 Perro tiene 5 años
```

Este método es ideal para realizar **efectos secundarios** como imprimir, guardar en base de datos o enviar información a la consola.

---

Podemos combinarlo con otros operadores para aplicar acciones sobre listas filtradas o transformadas:

```kotlin
// Mostramos solo los animales mayores de 10 años
animales
    .filter { it.edad > 10 }
    .forEach { println("🦴 ${it.nombre} es muy viejo!") }
```

Salida:

```
🦴 Tortuga es muy viejo!
```

### 🧮 reduce

El operador `reduce` sirve para **acumular los valores de una colección en un único resultado**.
Toma una función que combina progresivamente los elementos de la lista usando un **acumulador** (`acc`).

---

```kotlin
// Queremos sumar las edades de todos los animales
val sumaEdades = animales.map { it.edad }.reduce { acc, edad -> acc + edad }
println(sumaEdades) // 133
```

👉 En este ejemplo, `reduce` toma el primer elemento como valor inicial y va sumando los siguientes:

1. Empieza con 8
2. Luego 8 + 120
3. Finalmente 128 + 5 → **133**

---

También podemos usarlo para combinar cadenas o realizar operaciones personalizadas:

```kotlin
// Unimos todos los nombres en una sola cadena
val nombresConcatenados = animales
    .map { it.nombre }
    .reduce { acc, nombre -> "$acc, $nombre" }

println(nombresConcatenados)
// León, Tortuga, Perro
```

### 🧾 fold

El operador `fold` funciona igual que `reduce`, pero permite **especificar un valor inicial** para el acumulador.
Esto lo hace más seguro y flexible, ya que puede aplicarse incluso a listas vacías.


```kotlin
// Sumamos las edades de los animales partiendo desde 0
val sumaTotal = animales.map { it.edad }.fold(0) { acc, edad -> acc + edad }
println(sumaTotal) // 133
```

Aquí `0` es el valor inicial del acumulador (`acc`).
El bloque `{ acc, edad -> acc + edad }` se ejecuta para cada elemento, acumulando el resultado.


Podemos usarlo también para crear estructuras más complejas:

```kotlin
// Creamos una cadena con los nombres de todos los animales, separada por guiones
val nombresUnidos = animales.fold("") { acc, animal ->
    if (acc.isEmpty()) animal.nombre else "$acc - ${animal.nombre}"
}

println(nombresUnidos)
// León - Tortuga - Perro
```


La principal ventaja de `fold` sobre `reduce` es que siempre devuelve un valor,
incluso si la colección está vacía, ya que el acumulador inicial garantiza un resultado coherente.


### 🪆 flatMap

El operador `flatMap` se utiliza para **transformar cada elemento en una lista** y luego **aplanar** todas esas listas en una sola.
Es muy útil cuando una colección contiene sublistas o queremos generar varias salidas por cada elemento.


```kotlin
// Supongamos que cada animal tiene una lista de alimentos favoritos
data class Animal(val nombre: String, val edad: Int, val alimentos: List<String>)

val animales = listOf(
    Animal("León", 8, listOf("Carne", "Huesos")),
    Animal("Tortuga", 120, listOf("Lechuga", "Zanahoria")),
    Animal("Perro", 5, listOf("Pienso", "Huesos"))
)

// Queremos obtener una lista con todos los alimentos de todos los animales
val todosLosAlimentos = animales.flatMap { it.alimentos }

println(todosLosAlimentos)
// [Carne, Huesos, Lechuga, Zanahoria, Pienso, Huesos]
```


Podemos combinarlo con otros operadores funcionales:

```kotlin
// Obtenemos solo los alimentos únicos en mayúsculas
val alimentosUnicos = animales
    .flatMap { it.alimentos }
    .map { it.uppercase() }
    .distinct()

println(alimentosUnicos)
// [CARNE, HUESOS, LECHUGA, ZANAHORIA, PIENSO]
```


`flatMap` es ideal cuando **una transformación produce varias salidas por elemento**,
y queremos trabajar con todas ellas en una lista plana y unificada.


### 🧺 groupBy

El operador `groupBy` permite **agrupar los elementos de una colección según una clave**.
Devuelve un mapa (`Map<K, List<V>`) donde cada clave está asociada a la lista de elementos que comparten esa característica.

```kotlin
// Cada animal pertenece a una categoría


val animales = listOf(
    Animal("León", 8, "Mamífero"),
    Animal("Perro", 5, "Mamífero"),
    Animal("Tortuga", 120, "Reptil"),
    Animal("Cocodrilo", 50, "Reptil")
)

// Agrupamos los animales por tipo
val agrupadosPorTipo = animales.groupBy { it.tipo }

println(agrupadosPorTipo)
```

Salida:

```
{Mamífero=[Animal(nombre=León, edad=8, tipo=Mamífero), Animal(nombre=Perro, edad=5, tipo=Mamífero)], 
 Reptil=[Animal(nombre=Tortuga, edad=120, tipo=Reptil), Animal(nombre=Cocodrilo, edad=50, tipo=Reptil)]}
```

También podemos recorrer el mapa resultante para mostrarlo de forma más clara:

```kotlin
agrupadosPorTipo.forEach { (tipo, lista) ->
    println("🐾 Tipo: $tipo")
    lista.forEach { println(" - ${it.nombre} (${it.edad} años)") }
}
```

Salida:

```
🐾 Tipo: Mamífero
 - León (8 años)
 - Perro (5 años)
🐾 Tipo: Reptil
 - Tortuga (120 años)
 - Cocodrilo (50 años)
```

### 🗝️ associateBy

El operador `associateBy` nos permite **crear un mapa a partir de una lista**, usando una propiedad de los elementos como **clave**.
Cada clave estará asociada directamente a su objeto correspondiente.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val animales = listOf(
    Animal("León", 8, "Mamífero"),
    Animal("Perro", 5, "Mamífero"),
    Animal("Tortuga", 120, "Reptil")
)

// Creamos un mapa donde la clave es el nombre del animal
val mapaPorNombre = animales.associateBy { it.nombre }

println(mapaPorNombre)
```

Salida:

```
{León=Animal(nombre=León, edad=8, tipo=Mamífero), 
 Perro=Animal(nombre=Perro, edad=5, tipo=Mamífero), 
 Tortuga=Animal(nombre=Tortuga, edad=120, tipo=Reptil)}
```

Una vez creado el mapa, podemos acceder directamente a los objetos por su clave:

```kotlin
val leon = mapaPorNombre["León"]
println(leon?.edad) // 8
```

También es posible usar cualquier otra propiedad o incluso una expresión como clave:

```kotlin
// Asociamos por tipo
val mapaPorTipo = animales.associateBy { it.tipo }
println(mapaPorTipo["Reptil"])
// Animal(nombre=Tortuga, edad=120, tipo=Reptil)
```

### 📊 sortedBy

El operador `sortedBy` se usa para **ordenar una lista según una propiedad** de sus elementos.
Devuelve una **nueva lista ordenada**, sin modificar la original.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val animales = listOf(
    Animal("León", 8, "Mamífero"),
    Animal("Perro", 5, "Mamífero"),
    Animal("Tortuga", 120, "Reptil"),
    Animal("Cocodrilo", 50, "Reptil")
)

// Ordenamos por edad ascendente
val ordenadosPorEdad = animales.sortedBy { it.edad }

println(ordenadosPorEdad)
```

Salida:

```
[Animal(nombre=Perro, edad=5, tipo=Mamífero), 
 Animal(nombre=León, edad=8, tipo=Mamífero), 
 Animal(nombre=Cocodrilo, edad=50, tipo=Reptil), 
 Animal(nombre=Tortuga, edad=120, tipo=Reptil)]
```

Si queremos el orden inverso, podemos usar `sortedByDescending`:

```kotlin
val ordenadosDesc = animales.sortedByDescending { it.edad }
println(ordenadosDesc.first())
// Animal(nombre=Tortuga, edad=120, tipo=Reptil)
```


### 🧩 distinctBy

El operador `distinctBy` permite **eliminar elementos duplicados de una lista** según un criterio o propiedad.  
Devuelve una nueva lista con solo el **primer elemento único** encontrado para cada valor de la clave especificada.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val animales = listOf(
    Animal("León", 8, "Mamífero"),
    Animal("Perro", 5, "Mamífero"),
    Animal("Perro", 7, "Mamífero"),
    Animal("Tortuga", 120, "Reptil"),
    Animal("Tortuga", 100, "Reptil")
)

// Eliminamos duplicados por nombre
val sinDuplicados = animales.distinctBy { it.nombre }

println(sinDuplicados)
```

Salida:
```
[Animal(nombre=León, edad=8, tipo=Mamífero), 
 Animal(nombre=Perro, edad=5, tipo=Mamífero), 
 Animal(nombre=Tortuga, edad=120, tipo=Reptil)]
```

También podemos aplicar `distinctBy` con cualquier otro criterio, por ejemplo el tipo:

```kotlin
val unAnimalPorTipo = animales.distinctBy { it.tipo }
println(unAnimalPorTipo)
// [Animal(nombre=León, edad=8, tipo=Mamífero), Animal(nombre=Tortuga, edad=120, tipo=Reptil)]
```

### ✅ any, all y none

Estas tres funciones nos permiten **verificar condiciones sobre los elementos de una colección** y devuelven un valor booleano (`true` o `false`):

* `any` → Comprueba si **al menos un elemento** cumple la condición.
* `all` → Comprueba si **todos los elementos** la cumplen.
* `none` → Comprueba si **ningún elemento** la cumple.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val animales = listOf(
    Animal("León", 8, "Mamífero"),
    Animal("Perro", 5, "Mamífero"),
    Animal("Tortuga", 120, "Reptil")
)

// ¿Hay algún animal mayor de 100 años?
val hayMuyViejo = animales.any { it.edad > 100 }
println(hayMuyViejo) // true

// ¿Todos los animales son mamíferos?
val todosMamiferos = animales.all { it.tipo == "Mamífero" }
println(todosMamiferos) // false

// ¿Ningún animal tiene menos de 3 años?
val ningunoJoven = animales.none { it.edad < 3 }
println(ningunoJoven) // true
```

Estas funciones son muy útiles para validar condiciones de forma expresiva y concisa,
sin necesidad de usar bucles `for` ni estructuras `if` anidadas.


## 💡 Funciones de ámbito (Scope functions)


### ✨ let

El operador `let` se utiliza para **ejecutar un bloque de código con un objeto como contexto**,
lo que permite acceder a sus propiedades sin repetir su nombre.
Además, es muy útil para trabajar con valores **opcionales (nullable)** de forma segura.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val leon = Animal("León", 8, "Mamífero")

// Usamos let para ejecutar código con el objeto como contexto
leon.let {
    println("El animal es ${it.nombre}")
    println("Tiene ${it.edad} años y es un ${it.tipo}")
}
```

Salida:

```
El animal es León
Tiene 8 años y es un Mamífero
```

También puede combinarse con el operador seguro `?.` para evitar errores por `null`:

```kotlin
val tortuga: Animal? = null

// Solo se ejecuta el bloque si tortuga no es nula
tortuga?.let {
    println("La tortuga se llama ${it.nombre}")
} ?: println("No hay tortuga 🐢")
```

`let` es ideal para **encadenar operaciones** o **aislar bloques de código** sin crear variables temporales.

### ⚙️ run

El operador `run` se utiliza para **ejecutar un bloque de código y devolver su resultado**.
Es similar a `let`, pero su valor de retorno es **el resultado del bloque**, no el propio objeto.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val perro = Animal("Perro", 5, "Mamífero")

// Usamos run para ejecutar un bloque y devolver el resultado final
val descripcion = perro.run {
    println("Procesando información del animal...")
    "El $nombre es un $tipo de $edad años."
}

println(descripcion)
```

Salida:

```
Procesando información del animal...
El Perro es un Mamífero de 5 años.
```

También puede utilizarse con objetos opcionales (`nullables`):

```kotlin
val tortuga: Animal? = null

val info = tortuga?.run {
    "$nombre tiene $edad años"
} ?: "No hay información disponible"

println(info)
// No hay información disponible
```

`run` es muy útil cuando queremos **realizar operaciones sobre un objeto y obtener un valor de salida** sin necesidad de variables adicionales.

### 🧭 with

La función `with` se utiliza para **ejecutar un bloque de código sobre un objeto específico**,  
permitiendo acceder directamente a sus propiedades sin usar `it` ni repetir el nombre del objeto.  
A diferencia de `let` o `run`, **no es una extensión**, sino una función normal que recibe el objeto como parámetro.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val leon = Animal("León", 8, "Mamífero")

// Usamos with para acceder a las propiedades del objeto directamente
val resumen = with(leon) {
    println("Analizando animal...")
    "El $nombre es un $tipo de $edad años."
}

println(resumen)
```

Salida:
```
Analizando animal...
El León es un Mamífero de 8 años.
```

`with` es útil cuando queremos realizar **múltiples operaciones sobre el mismo objeto**  
sin repetir su nombre constantemente, mejorando la legibilidad del código.

### 🪄 apply

El operador `apply` se usa para **configurar o inicializar un objeto**,
ya que permite acceder a sus propiedades dentro de un bloque y luego **devuelve el propio objeto**.
Es muy útil cuando creamos instancias que necesitan varias asignaciones seguidas.

```kotlin
data class Animal(var nombre: String, var edad: Int, var tipo: String)

// Creamos y configuramos un objeto con apply
val gato = Animal("", 0, "").apply {
    nombre = "Gato"
    edad = 3
    tipo = "Mamífero"
}

println(gato)
// Animal(nombre=Gato, edad=3, tipo=Mamífero)
```

Podemos combinarlo con otras operaciones para construir objetos de forma fluida:

```kotlin
val perro = Animal("Perro", 5, "Mamífero").apply {
    println("Inicializando objeto...")
    edad += 1
    nombre = nombre.uppercase()
}

println(perro)
// Animal(nombre=PERRO, edad=6, tipo=Mamífero)
```

`apply` es ideal para **configurar objetos**, especialmente en inicializaciones o builders,
ya que evita repeticiones y mejora la claridad del código.

### 🔄 also

El operador `also` se utiliza para **realizar una acción adicional sobre un objeto**
sin modificar su valor ni interrumpir su cadena de operaciones.
Devuelve **el mismo objeto**, lo que permite continuar encadenando llamadas.

```kotlin
data class Animal(val nombre: String, val edad: Int, val tipo: String)

val perro = Animal("Perro", 5, "Mamífero").also {
    println("🐶 Creado un nuevo animal: ${it.nombre}")
}

println(perro)
// Animal(nombre=Perro, edad=5, tipo=Mamífero)
```

La diferencia con `let` o `run` es que `also` **devuelve el objeto original**,
no el resultado del bloque. Por eso se usa mucho para depuración o trazas.

```kotlin
val leon = Animal("León", 8, "Mamífero")
    .also { println("Antes de modificar: $it") }
    .copy(edad = 9)
    .also { println("Después de modificar: $it") }
```

Salida:

```
Antes de modificar: Animal(nombre=León, edad=8, tipo=Mamífero)
Después de modificar: Animal(nombre=León, edad=9, tipo=Mamífero)
```

`also` resulta perfecto cuando queremos **ejecutar efectos secundarios**
(como imprimir, registrar o validar) dentro de una cadena funcional,
sin alterar el flujo principal del programa.



---

## 🧠 Resumen


### 🧩 Funciones funcionales sobre colecciones (`Iterable`, `List`, etc.)

| Función              | Descripción                                                   | Ejemplo                              |
| -------------------- | ------------------------------------------------------------- | ------------------------------------ |
| `map`                | Transforma cada elemento en otro.                             | `list.map { it * 2 }`                |
| `filter`             | Filtra los elementos que cumplen una condición.               | `list.filter { it > 0 }`             |
| `forEach`            | Ejecuta una acción por cada elemento (no devuelve resultado). | `list.forEach { println(it) }`       |
| `reduce`             | Acumula los valores en un único resultado.                    | `list.reduce { acc, n -> acc + n }`  |
| `fold`               | Igual que `reduce`, pero con valor inicial.                   | `list.fold(0) { acc, n -> acc + n }` |
| `flatMap`            | “Aplana” listas de listas en una sola.                        | `list.flatMap { it.subList(0, 2) }`  |
| `groupBy`            | Agrupa elementos por una clave.                               | `list.groupBy { it.tipo }`           |
| `associateBy`        | Crea un mapa usando una propiedad como clave.                 | `list.associateBy { it.id }`         |
| `sortedBy`           | Ordena por una propiedad.                                     | `list.sortedBy { it.edad }`          |
| `distinctBy`         | Elimina duplicados según un criterio.                         | `list.distinctBy { it.nombre }`      |
| `any`, `all`, `none` | Verifican condiciones sobre la colección.                     | `list.any { it > 5 }`                |

👉 **Todas estas son funciones puras**, sin efectos secundarios, y reflejan **programación funcional**.

---

### 💡 Funciones de ámbito (`Scope functions`)

Estas son muy típicas del estilo **funcional-expresivo** de Kotlin. Permiten ejecutar bloques sobre un objeto y devolver algo útil.

| Función | Devuelve             | Cuándo usarla                                                  | Ejemplo                                      |
| ------- | -------------------- | -------------------------------------------------------------- | -------------------------------------------- |
| `let`   | Resultado del bloque | Cuando quieres operar sobre un valor temporal o evitar `null`. | `x?.let { it * 2 }`                          |
| `run`   | Resultado del bloque | Cuando trabajas con inicialización compleja.                   | `run { val x = 10; x + 5 }`                  |
| `with`  | Resultado del bloque | Para evitar repetir el nombre de un objeto.                    | `with(obj) { println(nombre) }`              |
| `apply` | El mismo objeto      | Para configurar objetos (builder).                             | `val p = Persona().apply { nombre = "Ana" }` |
| `also`  | El mismo objeto      | Para ejecutar efectos secundarios (logs, prints).              | `list.also { println(it.size) }`             |



---

## 🚀 Conclusión

La **programación funcional** en Kotlin nos permite escribir código más **limpio, expresivo e inmutable**.
Operadores como `map`, `copy` y `let` son pilares fundamentales que nos ayudan a **transformar datos, crear objetos derivados y evitar errores con valores nulos**.

Practica combinando estos operadores y verás cómo tu código se vuelve más claro, potente y elegante. ✨
