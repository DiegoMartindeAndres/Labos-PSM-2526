# 🚀 Guía sobre la Gestión de Estado

En esta primera guía sobre la gestión de estado, haremos una introducción con un ejemplo sencillo creando un pequeño juego sobre la gestión logística de paquetes, y luego pasaremos a nuestro proyecto sobre la conversión de unidades.

## Tabla de Contenidos 📚

- [🚀 Guía sobre la Gestión de Estado](#-guía-sobre-la-gestión-de-estado)
- [📦 Introducción: Gestión Logística en Jetpack Compose](#-introducción-gestión-logística-en-jetpack-compose)


# 📦 Introducción: Gestión Logística en Jetpack Compose

Imagina que eres el gerente de una empresa de logística que entrega paquetes. El estado de los paquetes y las rutas de entrega es crucial para tomar decisiones. Aquí es donde entra en juego la **gestión del estado**. En Jetpack Compose, el manejo del estado es como un sistema de gestión logística que guía la dirección de tu aplicación cuando ocurren eventos, como acciones del usuario o actualizaciones de datos.

Los eventos actúan como señales para ajustar el estado de la aplicación, de la misma manera que un gerente ajusta las rutas de entrega en respuesta a condiciones cambiantes.

---

## 📦 Gestión del Estado y Eventos

### 🚚 ¿Qué es la Gestión del Estado?

La gestión del estado y los eventos trabajan juntos para mantener tu aplicación en curso a través del dinámico paisaje de las interacciones del usuario. La idea principal es que cuando ocurre un evento (por ejemplo, el usuario toca un botón), se actualiza el estado, y esto desencadena una **recomposición** de la interfaz de usuario para reflejar los cambios.

### 🔄 ¿Qué es la Re-composición?

En Jetpack Compose, la interfaz de usuario se representa como un árbol de **Composables**. La **recomposición** es el proceso de regenerar y actualizar la interfaz de usuario para reflejar los cambios en el estado de la aplicación o las interacciones del usuario. Este proceso es eficiente y permite mantener la interfaz siempre actualizada.

---

## 📌 Conceptos Clave en la Gestión del Estado

### 🧠 Función `remember`

La función `remember` se utiliza para crear un estado **persistente y recordado** en Jetpack Compose. Esto permite que un Composable mantenga su estado a través de las recomposiciones, incluso cuando el Composable es recreado. Es crucial para evitar la pérdida de datos y el estado de la interfaz de usuario durante los ciclos de vida de la aplicación.

La función `remember` se recomienda para mantener el estado local de un Composable. Sin embargo, si el estado necesita ser compartido entre múltiples Composables, entonces se debe **elevar el estado** para que sea administrado por un componente superior en la jerarquía.

En otras palabras, `remember` es como un recordatorio interno del Composable. Imagina que estás haciendo una entrega y necesitas recordar cuántos paquetes has entregado. `remember` ayuda al Composable a "recordar" ese número incluso si algo cambia en la interfaz, como si fuera tu lista de paquetes entregados.

```kotlin
val count = remember { mutableStateOf(0) }
```

### 🛠 Delegado de Propiedad `mutableStateOf`

El delegado de propiedad `mutableStateOf` se utiliza para crear un estado mutable que puede ser actualizado y que, cuando cambia, provoca una recomposición del Composable que lo utiliza. Esto garantiza que los cambios en los datos se reflejen automáticamente en la interfaz de usuario.

En general, `mutableStateOf` se usa junto con `remember` para gestionar datos que cambian dentro de un Composable específico. Es como marcar un paquete como "listo para la entrega"; cualquier cambio en el estado de ese paquete (como si ha sido entregado o no) se reflejará automáticamente en tu sistema.

```kotlin
val text = remember { mutableStateOf("Hola, Jetpack Compose!") }
```

### 🔄 Integrando `remember` y `mutableStateOf`

Estos dos conceptos funcionan en conjunto:
- `remember` crea un estado que persiste a través de las recomposiciones.
- `mutableStateOf` permite actualizar el valor del estado y, cuando esto ocurre, se dispara la recomposición del Composable.

### ⬆️ Elevación del Estado

En aplicaciones más complejas, es posible que múltiples Composables necesiten acceder o modificar el mismo estado. En estos casos, es recomendable **elevar el estado** a un componente superior. Esto permite que el estado se administre de manera centralizada, lo cual facilita la sincronización de la interfaz de usuario y la lógica de la aplicación.

Elevación del estado significa mover la información relevante a un nivel más alto para que otros componentes puedan acceder a ella. Imagina que en tu empresa de logística todos necesitan acceso al inventario de paquetes. En lugar de que cada equipo tenga su propio inventario, el estado se eleva para que todos puedan consultar el inventario actualizado y tomar decisiones basadas en la misma información.

La gestión del estado en Jetpack Compose es esencial para crear aplicaciones reactivas y dinámicas. Al entender cómo utilizar `remember`, `mutableStateOf` y cuándo elevar el estado, puedes diseñar interfaces eficientes que se actualicen automáticamente en respuesta a los cambios del usuario o de los datos.

Recuerda que en aplicaciones más grandes y complejas, el estado debe ser manejado cuidadosamente para mantener el flujo de datos limpio y evitar inconsistencias. Jetpack Compose te proporciona herramientas poderosas para que la interfaz de usuario esté siempre sincronizada con el estado actual de la aplicación.


## 👀 Vistazo Rápido a los Estados en Compose

El concepto de **state** (estado) es fundamental en Compose. Este define cómo una interfaz se debe actualizar cuando cambian los datos. Hay dos tipos principales de estados:

- **Immutable State**: No puede cambiar después de ser creado.
- **Mutable State**: Puede cambiar, permitiendo que la interfaz se actualice automáticamente.

Para gestionar el estado mutable, Jetpack Compose ofrece la función `remember` junto con `mutableStateOf`. Esto nos permite guardar y recordar el valor de una variable, permitiendo que los cambios se reflejen automáticamente en la interfaz.

## 🎢 El Juego de los paquetes
En el ejemplo práctico que desarrollamos, se crea un proyecto simple llamado **Juego Paquetes**. La aplicación tiene tres componentes principales:

1. **Un texto que muestra la cantidad de paquetes encontrados**.
2. **Un texto que indica la dirección en la que se estamos navegando**.
3. **Cuatro botones** que permiten cambiar la dirección (norte, sur, este, oeste) y que otorgan una oportunidad de encontrar un paquete o enfrentarse a una tormenta.

## 📜 Código en Jetpack Compose

Para ilustrar cómo funciona el código, se presenta un ejemplo en el cual creamos una función composable para el juego de los paquetes:

```kotlin
@Composable
fun JuegoPaquetes() {
    val packagesFound = remember { mutableStateOf(0) }
    val direction = remember { mutableStateOf("Norte") }

    Column {
        Text(text = "Paquetes encontrados: ${packagesFound.value}")
        Text(text = "Dirección actual: ${direction.value}")

        Button(onClick = {
            direction.value = "Este"
            if (Random.nextBoolean()) { // 50% de probabilidad de encontrar un paquete
                packagesFound.value += 1
            }
        }) {
            Text("Ir al Este")
        }
        // Repetir para los otros botones (Oeste, Norte, Sur)
    }
}
```

### Explicación del Código
- `remember { mutableStateOf(0) }`: Esto nos permite recordar el número de paquetes que hemos encontrado. Utilizamos `remember` y `mutableStateOf` para que el valor se pueda actualizar y se refleje en la interfaz. Aunque pensemos que nos valdría con un `Int` y un `String`, necesitamos usar `remember` y `mutableStateOf` para que Compose sepa cuándo actualizar la interfaz.
- `Button(onClick = {...})`: Cada vez que hacemos clic en un botón, actualizamos la dirección del la furgoneta que lleva los paquetes y tenemos una oportunidad de encontrar un paquete usando `Random.nextBoolean()`, que devuelve `true` o `false` al azar.

## 🌐 La Magia de los Estados
Uno de los conceptos clave que se ilustra aquí es cómo **Jetpack Compose se encarga de actualizar la interfaz** cuando los valores cambian. Por ejemplo, al cambiar `packagesFound.value`, Compose detecta este cambio y actualiza el componente `Text` correspondiente sin necesidad de decirle explícitamente a la interfaz que cambie.

Esto se debe a que `mutableStateOf` no solo guarda el valor, sino que también informa a Compose sobre cualquier cambio, lo que permite mantener la interfaz actualizada de manera sencilla.

## 🛡️ Tips Importantes
- **Val vs Var**: En Kotlin, `val` define una constante, mientras que `var` define una variable. Sin embargo, cuando usamos `mutableStateOf` con `val`, lo que no cambia es la referencia al objeto mutable. El valor interno puede cambiar sin problemas.
- **Recuerda Usar `remember`**: Es importante usar `remember` para que Compose pueda guardar y restaurar el estado durante la recomposición.
 
 ---
 
🛠️ **Desafío 1**: Si no encontramos un tesoro, tenemos un 1 probabilidad entre 4 de encontrarnos una tormenta. Añade un nuevo componente que muestre las tormentas que nos hemos ido encontrando.

🛠️ **Desafío 2**: Añade un nuevo componente que muestre si nuestra furgoneta de reparto está en buena o mala condición. Una furgoneta empieza con 100 puntos de vida y cada tormenta que nos encontramos nos quita 5 puntos de vida.


 
## ✨ Recordar y gestionar el estado

En Jetpack Compose, es habitual almacenar el estado de las UI usando la función `remember` junto con `mutableStateOf()`. Estos conceptos son fundamentales para crear componentes que reaccionen de manera dinámica a los cambios.

Por ejemplo:

```kotlin
val paquetesEncontrados = remember { mutableStateOf(0) }
```

La función `remember` se utiliza para almacenar un valor que necesita persistir durante la recomposición de la UI. En este caso, `mutableStateOf(0)` es el valor inicial del estado. Cada vez que este valor cambia, la UI se actualizará de manera automática.

Para acceder al valor del estado, utilizamos `.value`:

```kotlin
val cantidad = paquetesEncontrados.value
```

Y en nuestro código se traduce en:

```kotlin
Text(text = "Paquetes encontrados: ${packagesFound.value}")
```

Pero también podemos utilizar el operador `by`

## ✂️ Simplificando con el operador `by`

Hay una forma más sencilla de escribir este código, utilizando el operador `by`. Esto nos permite evitar el uso constante de `.value` para acceder al valor del estado.

Primero tendremos que importar `import androidx.compose.runtime.setValue` 

```kotlin
val paquetesEncontrados by remember { mutableStateOf(0) }
```

Con `by`, accedemos directamente al valor del estado sin necesidad de usar `.value`. En el ejemplo anterior:

- `paquetesEncontrados` ahora contiene directamente el valor.
- No necesitamos usar `paquetesEncontrados.value` para acceder al número de paquetes encontrados.

Ahora podemos usar directamente:

```kotlin	
Text(text = "Paquetes encontrados: $paquetesEncontrados")
```

### 🎁 Metáfora con los paquetes

Imagina que `paquetesEncontrados` es un paquete con objetos dentro:

- Sin usar `by`, necesitamos abrir el paquete para ver lo que hay dentro (`.value`).
- Usando `by`, el producto está directamente en nuestras manos, sin necesidad de abrir el paquete.

## 🔧 Solución

🎉 ¡Enhorabuena! Si has llegado hasta aquí, ya tienes tu **segunda interfaz** creada en **Jetpack Compose**. ✨  
A continuación tienes el **código completo** de la aplicación, pero recuerda: su propósito es **aprender y experimentar**, no simplemente copiar y pegar.  
💡 **Ejecutarlo sin comprenderlo no te servirá de nada** — intenta analizarlo, modificarlo y entender cómo funciona cada parte. 👀


<details>
  <summary>Haz clic para ver el código</summary>

```kotlin
package es.uva.inf5g.psm.juegopaquetes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uva.inf5g.psm.juegopaquetes.ui.theme.JuegoPaquetesTheme
import kotlin.math.max
import kotlin.random.Random

private const val STORM_DAMAGE = 5
private const val BAD_CONDITION_THRESHOLD = 60 // Cambia este umbral si quieres

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JuegoPaquetesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    JuegoPaquetes(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun JuegoPaquetes(modifier: Modifier = Modifier) {
    var packagesFound by remember { mutableStateOf(0) }
    var direction by remember { mutableStateOf("Norte") }

    // 🛠️ Desafío 1 & 2: estado adicional
    var stormsFound by remember { mutableStateOf(0) }
    var vanHealth by remember { mutableStateOf(100) }

    fun handleMove(newDirection: String) {
        direction = newDirection
        // 50% de encontrar paquete
        val foundPackage = Random.nextBoolean()
        if (foundPackage) {
            packagesFound += 1
            return
        }
        // Si NO hay paquete ➜ 1 entre 4 de tormenta
        val storm = Random.nextInt(4) == 0
        if (storm) {
            stormsFound += 1
            vanHealth = max(0, vanHealth - STORM_DAMAGE)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Paquetes encontrados: $packagesFound", style = MaterialTheme.typography.titleMedium)
        Text(text = "Dirección actual: $direction", style = MaterialTheme.typography.bodyLarge)

        // 🛠️ Desafío 1: panel de tormentas
        StormsPanel(stormsFound = stormsFound)

        // 🛠️ Desafío 2: estado de la furgoneta
        VanStatus(health = vanHealth)

        // Controles de movimiento en cuadrícula 2x2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { handleMove("Norte") }) { Text("Ir al Norte") }
                Button(onClick = { handleMove("Sur") }) { Text("Ir al Sur") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { handleMove("Este") }) { Text("Ir al Este") }
                Button(onClick = { handleMove("Oeste") }) { Text("Ir al Oeste") }
            }
        }

        // (Opcional) Botón de reinicio
        OutlinedButton(onClick = {
            packagesFound = 0
            direction = "Norte"
            stormsFound = 0
            vanHealth = 100
        }) {
            Text("Reiniciar")
        }
    }
}

@Composable
fun StormsPanel(stormsFound: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "⛈️ Tormentas encontradas: $stormsFound",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun VanStatus(health: Int, modifier: Modifier = Modifier) {
    val condition = if (health >= BAD_CONDITION_THRESHOLD) "Buena" else "Mala"
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "🚐 Estado de la furgoneta: $condition")
        Text(text = "Vida: $health/100")
    }
}

@Preview(showBackground = true)
@Composable
fun JuegoPaquetesPreview() {
    JuegoPaquetesTheme {
        Scaffold { innerPadding ->
            JuegoPaquetes(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        }
    }
}



```

</details>