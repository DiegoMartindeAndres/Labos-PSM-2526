# 🔄 Continuación con la aplicación de conversión de unidades

Vamos a retomar nuestra aplicación de conversión de unidades. Hasta ahora, hemos trabajado en la interfaz gráfica (GUI), pero en los siguientes apartados implementaremos la lógica necesaria para que funcione correctamente.

<!-- 
Nota: aunque en clase hemos visto el MVC y la evolución al MVVC, de momento no vamos a usar ningún patrón de diseño concreto. En futuros laboratorios, profundizaremos en el concepto MVVC y cómo aplicarlo en aplicaciones de Android.
 -->



## 📌 Variables de Estado Necesarias
Vamos a pensar por un momento las variables que vamos a necesitar para que nuestra aplicación funcione correctamente. 

Serías capaz de pensar en las variables de estado necesarias para que nuestra aplicación funcione correctamente?

<details>
  <summary>Pincha aquí para ver esas variables.</summary>
<br>
Para crear un conversor de unidades usando Compose, necesitaremos definir varias variables de estado 🔧:

- **Valor de entrada**: representa el valor que el usuario introduce para convertir (se ingresa en el cuadro de texto).
- **Valor de salida**: resultado de la conversión (no se puede modificar).
- **Unidad de entrada**: la unidad de origen (seleccionada en el primer desplegable).
- **Unidad de salida**: la unidad de destino (seleccionada en el segundo desplegable).

⚠️ ¡Ojo! Estas no son todas las variables necesarias. Hay otras que se requieren para que la interfaz y la lógica funcionen correctamente. Son menos evidentes ahora, pero las iremos añadiendo a medida que las necesitemos. Si ya puedes identificar algunas, ¡enhorabuena! Eso significa que tienes una visión más amplia de lo que necesitamos para que nuestra aplicación funcione.
</details>
<br>


Ahora vamos a pensar como definir esas variables usando `remember`, `mutableStateOf` y si queremos la palabra clave `by`:

<details>
  <summary>Pincha aquí para ver la definición de dichas varibles.</summary>
  
```kotlin
var inputValue by remember { mutableStateOf("") }
var outputValue by remember { mutableStateOf("") }
// Inicializamos el valor inicial de la unidad de entrada y salida
var inputUnit by remember { mutableStateOf("Centímetro") }  
var outputUnit by remember { mutableStateOf("Metros") }
// Habrá más varibles, pero de momento son difíciles de intuir  
```
</details>
<br>

Nota 1: ¿Dónde las definimos? - Al principio de la función `@Composable` que contiene la interfaz gráfica. En mi caso la llamé `ConversorUnidades`.	

Nota 2: Es posible que tengas que hacer importacines para poder usar `remember` y `mutableStateOf`.

Con estas varibles podemos controlar los datos de entrada que introduce el usuario, la unidad de entrada y salida, y el resultado de la conversión. Ahora lo que tenemos es que enlazarlo con la interfaz gráfica.

## 🖊️ OutlinedTextField que nunca cambia su valor

En la interfaz gráfica, tenemos un `OutlinedTextField` que debería mostrar lo que el usuario introduce por teclado. Sin embargo, este campo siempre aparece vacío, sin importar lo que escribamos. 

Aunque puede resultar sorprendente, este comportamiento es normal, ya que no hemos programado nada para que el valor cambie. Tampoco hemos vinculado nuestro modelo (por ahora, solo una variable) con la interfaz gráfica.

Hemos creado una variable `inputValue` que almacena el valor introducido por el usuario. Es momento de enlazarla con la GUI.

¿Cómo lo haremos? Primero, debemos indicar que el `value` del `OutlinedTextField` es `inputValue` y asegurarnos de que cuando `inputValue` cambie, se actualice la interfaz gráfica.

```kotlin	
OutlinedTextField(
    value = inputValue,
    onValueChange = { inputValue = it },
    label = { Text("Introduce el valor") }
)
```

¿Pero qué significa `onValueChange = { inputValue = it }`?

Recuerda, es una función lambda que se ejecuta cada vez que hay un cambio de valor. De este modo, `inputValue` tomará el valor de `it`, que es el nuevo valor pasado a la función lambda.

Nota: Esto no funcionará en la vista previa, solo en la aplicación en ejecución.

Con esto, hemos enlazado el valor del modelo y el de la interfaz gráfica. Ahora, cada vez que el usuario introduzca un valor, se actualizará automáticamente en el modelo y en la interfaz gráfica.

## 🔄 Sincronización del Estado
El estado debe mantenerse sincronizado entre diferentes componentes. Por ejemplo, al cambiar el valor en el campo de texto, debe actualizarse automáticamente en el resto de la interfaz. Esta es una de las ventajas de usar Compose, ya que hace que la interfaz de usuario sea reactiva y siempre coherente.

## 🎛️ Creación de botones y menús desplegables

Comencemos con la configuración básica de los botones. Tenemos dos botones: uno para la **entrada** (input) y otro para la **salida** (output). Cada botón está asociado a un **dropdown** que se debería expandir o colapsar según la interacción del usuario.
Aunque en realidad no hace nada. Está ocurriendo lo mismo que el OutlineTextField, no tenemos nada programado y debemos enlazar las variables.

Ahora nos deberíamos dar cuenta que necesitamos una variable adicional para cada botón, necesitamos definir un estado que controle si el menú desplegable está expandido o no.

```kotlin
// Definición de estados para los menús desplegables
var inputExpanded by remember { mutableStateOf(false) }
var outputExpanded by remember { mutableStateOf(false) }
```

Hemos creado dos variables booleanas, `inputExpanded` y `outputExpanded`, que almacenan los valores de como debería estar el estado de cada **dropdown**

## 🏰 Implementación del botón de entrada

¿Cuando queremos que se explanda el primer **dropdown**? - Cuando hacemos clic en el botón de entrada. Para ello, necesitamos que al hacer clic en el botón de unidades de origen, cambie el estado de `inputExpanded` a `true`.

```kotlin
Button(onClick = { inputExpanded = true }) {
    Text("Mostrar menú de entrada")
}
```

Pero no basta con esto. Al **Dropdown** correspondiente, debemos decirle que `expanded` es igual a `inputExpanded` .

```kotlin
DropdownMenu(
    expanded = inputExpanded,
...
)
```
Prueba a ver que pasa. Y verás que se expande el primer menú al hacer clic en el botón.	

Antes de continuar, vamos a implementar el otro **dropdown** de entrada. Usando la variable `outputExpanded` para controlar el estado del menú. Si lo pruebas, verás que hace cosas un poco extrañas porque aún no hemos implementado el colapso del menú. Verás que se expande el menú de entrada al hacer clic en el botón. Pero si vuelves a pulsar, no ocurre nada, sigue expandido.

Para colapsar el menú, necesitamos controlar el evento `onDismissRequest`. El evento `onDismissRequest` se activa cuando el usuario hace clic fuera del menú.


```kotlin
DropdownMenu(
    expanded = inputExpanded,
    onDismissRequest = { inputExpanded = false }
) {
    // Elementos del menú
}
```

Haz esto para los dos **dropdown**

Si lo pruebas, verás que si pulsamos fuera del **dropdown** el menú se colapsa.


## Implementación del `onClick` en los `DropdownMenuItem`

Cuando el usuario selecciona una opción del menú, debemos hacer varias cosas:

1. Actualizar el valor de la unidad de entrada o de salida.
2. Colapsar el menú desplegable.

Ahora nos damos cuenta, que si cambiamos las unidades de origen y las unidades de destino, podemos calcular una cosa que llamaremos **factor de conversión**, es decir, si la unidad de origen es **centímetros** y la de destino es **metros**, el factor de conversión será 0.01. Si la unidad de origen es **metros** y la de destino es **centímetros**, el factor de conversión será 100. 

Por lo tanto podemos hacer un paso nuevo:

3. Podemos calcular el factor de conversión. (Poniendo como unidad de referencia los metros.) NOTA: aunque claro, esto nos supone crear una variable nueva que no habíamos contado con ella: `var convertionFactor by remember { mutableStateOf(0.01) }`	

```kotlin
DropdownMenuItem(onClick = {
    inputExpanded = false
    inputUnit = "Centímetros"
    convertionFactor = 0.01
}) {
    Text("Centímetros")
}
```

Si lo pruebas, verás que en el primer **dropdown** al seleccionar **Centímetros**, se colapsa el menú (y se actualiza el valor de la unidad de entrada.) Si pulsas cualquier otra opción, no pasa nada.



## ⚙️ Conversión de Unidades

En este ejemplo, se realiza la conversión de diferentes unidades, como centímetros y metros. Para ello, se utiliza una función que actualiza el valor de conversión basado en la opción seleccionada.

```kotlin
fun convertirUnidades(unidadSeleccionada: String): Double {
    return when (unidadSeleccionada) {
        "Centímetros" -> 0.01
        "Metros" -> 1.0
        "Pies" -> 0.3048
        "Milímetros" -> 0.001
        else -> 1.0
    }
}
```

### 🚀 Ejecutar la Conversión

Cada vez que el usuario selecciona una opción en el menú, se actualiza el valor de la conversión y se colapsa el menú desplegable.

```kotlin
DropdownMenuItem(onClick = {
    val factorConversion = convertirUnidades("Centímetros")
    inputExpanded = false // Cierra el menú desplegable
    // Realiza la conversión con el factor correspondiente
}) {
    Text("Centímetros")
}
```

## 🚀 Creando una función de conversión de unidades

Vamos a implementar la función `convertUnits`, que se encargará de la lógica para convertir valores entre diferentes unidades.


### 📄 Implementación de `convertUnits`

La función `convertUnits` manejará la lógica de conversión de unidades en nuestra aplicación. La lógica es sencilla: multiplicar el valor de entrada y el factor de conversión.

### Ubicación de la función
Existen dos opciones:

1. Colocarla en la misma clase, pasando los parámetros necesarios, ya que no se podría acceder a las variables del `@Composable` directamente.  
2. Definirla como una función anidada dentro del `@Composable`, lo cual es posible en Kotlin y permite encapsular la lógica específica.

Para mayor conveniencia, colocaremos la función en el componente `@Composable` para encapsularla en el contexto adecuado.
Vamos a pensar, que debería hacer nuestro función? ¿Puedes pensar en el código que debería implementarse?


### ¿Qué debe hacer la función?
En pseudocódigo, los pasos serían:

- Tomar el valor de entrada.
- Intentar convertirlo a un Double, manejando errores si el valor no es un número.
- Multiplicarlo por el factor de conversión.
- Redondear el resultado a dos decimales.
- Almacenar el resultado en una variable para mostrarlo en la interfaz.

¿Te atreves a realizar la función?

<details>
  <summary>Pincha aquí para ver esas variables.</summary>
<br>

```kotlin
@Composable
fun ConversorUnidades() {
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }

fun convertUnits() {
        // Aquí tomamos el valor de entrada, intentamos convertir a un Double, si no puede, devolverá un null, pero con el operador Elvis, si es null, se le asigna un 0.0
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        
        // Hacemos el cálculo, redondeamos y guardamos el resultado
        val result = (inputValueDouble * convertionFactor * 100).roundToInt() / 100.0
        
        // Queremos que se muestre en la GUI, por lo tanto tenemos que convertirlo a String y asignarlo a la variable que es un mutableStateOf
        outputValue = result.toString()
    }

    // Aquí se implementa la interfaz
}
```

</details>
<br>

### 🧠 Lógica de redondeo

Para evitar resultados con demasiados decimales, se utiliza una técnica de redondeo:

```kotlin
val result = (inputValueDouble * conversionFactor * 100).roundToInt() / 100.0
```

Este código primero multiplica el valor de entrada por el factor de conversión y por `100`. Luego redondea el valor usando `roundToInt()`, y finalmente lo divide por `100` para obtener el resultado con dos decimales.

## 🎨 Modificando los elementos de la interfaz

Es importante llamar la función `convertUnits()` en los eventos `onClick` de **TODOS** los `DropdownMenuItem` de entrada. Así, cada vez que se seleccione una unidad diferente, se recalculará el valor convertido.

Ejemplo para el caso de los centímetros:

```kotlin
DropdownMenuItem(
    text = { Text("Centímetros") },
    onClick = {
        inputExpanded = false
        inputUnit = "Centímetros"
        convertionFactor = 0.01
        convertUnits()
    })
```

Es necesario ajustar los valores de conversión y las unidades según corresponda. Por ejemplo, los metros tienen un factor de `1.0` ya que suponemos que el destino es metros, por lo tanto la conversión es la unidad. Pero cuando queremos convertir de pies a metros, el factor de conversión será `0.3048`

### 🏹 Ajustando el Factor de Conversión
En el código de la aplicación, comenzamos definiendo los factores de conversión para las unidades:

- Para metros (💪), el valor predeterminado es `1`.
- Para centímetros, el valor será `0.01` ya que hay 100 cm en un metro.
- Para pies (🥾), utilizaremos un factor de `0.3048`, ya que un pie equivale a 0.3048 metros.
- Para milímetros, el valor será `0.001`, ya que hay 1000 mm en un metro.

### Houston tenemos un problema!!

Ya te debes estar dando cuenta que con un solo factor de conversión no es suficiente, ya que si queremos convertir de un origen a un destino, el factor de conversión no es el mismo que de metros a pies. Por lo tanto, necesitamos dos factores de conversión, uno para la unidad de entrada y otro para la unidad de salida.

Por lo tanto, vamos a refactorizar renombrando la variable `convertionFactor` a `inputConvertionFactor` y crearemos una nueva variable llamada `outputConvertionFactor`.

Para hacerlo más sencillo, vamos a inicializar las variables `inputConvertionFactor` y `outputConvertionFactor` con el valor `1.0` y eso nos forzará a inicializar `inputUnit` y `outputUnit` a Metros.

### 🔬 Retocar la función de los Cálculos de Conversión
Para realizar la conversión, tenemos que pensar como sería la nueva fórmula. Recuerda que tenemos el factor de conversión de la unidad de entrada y el de la unidad de salida. ¿Cómo lo harías?

<details>
  <summary>¿Puedes hacerlo sin ayuda?</summary>
<br>

Al valor de entrada, lo debemos multiplicar por el factor de conversión de entrada (esto ya lo teníamos) y dividir por el factor de conversión de salida. Además de multiplicar por cien y dividir por cien para calcular bien los decimales.

```kotlin
val result = (inputValueDouble * inputConvertionFactor * 100 / outputConvertionFactor).roundToInt() / 100.0
```

</details>
<br>


### 🌱 Drop-Down Menús para las unidades de salida.
Usamos menús desplegables para seleccionar las unidades de origen y destino. Es el momento de cambiar el código para cuando se selecciona una unidad de destino y que la aplicación recalcule automáticamente el valor convertido para mantener la coherencia y usabilidad de la interfaz. El código es muy parecido al evento onClick de los DropdownItem de las unidades de origen.

<details>
  <summary>¿Te atreves a realizar la función?</summary>

Aunque solo te voy a dar la función onClick, de dropdownMenuItem de metros.

```kotlin
onClick = {
    outputExpanded = false
    outputUnit = "Centímetros"
    inputConvertionFactor = 0.01
    convertUnits()
}
```

Tendrás que hacer lo mismo para las otras unidades.
</details>
<br>

## 💡 Cambio del texto de los botones.

Como te habrás fijado, al seleccionar una unidad de origen o destino, el texto de los botones no cambia y es un problema de usabilidad. ¿No sería deseable que cambiara cuando se selecciona las unidades? 

Vamos a cambiar el texto de los botones para que muestren la unidad seleccionada.

<details>
  <summary>¿Te atreves?</summary>

Te pongo el ejemplo de solo un botón.

```kotlin
Button(onClick = { inputExpanded = true }) {
    Text(text = inputUnit)
    //....
}

```

</details>
<br>


## 🚀 Prueba la aplicación.

¿Hace bien los cálculos? ¿Cambia correctamente las unidades de origen y destino? ¿Cambia el texto de los botones?

Debe de haber un pequeño problema aún. Si seleccionas una unidad de origen y destino, y cambias el valor de entrada, el valor de salida no cambia. ¿Por qué crees que es?

¿Qué habría que hacer?

<details>
  <summary>Solución</summary>
<br> 

Debemos llamar a la función `convertUnits()` en el final del evento `onValueChange` del `OutlinedTextField`. De esta forma, cada vez que se cambie el valor de entrada, se recalculará el valor convertido.

```kotlin
onValueChange = {
    inputValue = it
    convertUnits()
}
```
</details>
<br>

## ✨ Modificando el Estilo de la App

Vamos a cambiar la aparencia gráfica de nuestra aplicación, ya que los textos que tenemos son un poco pequeños.

Para cambiar la apariencia de un texto en Jetpack Compose, puedes usar la propiedad `style` del componente **Text**.

Por ejemplo, si tienes un resultado que deseas mostrar más grande, puedes usar una de las opciones de tipografía disponibles en **MaterialTheme**.

Por ejemplo, vamos a modificar el tamaño del texto del resultado:

```kotlin
Text(
    text = outputValue,
    style = MaterialTheme.typography.headlineMedium
)
```
En este ejemplo, estamos usando `headlineMedium` para agrandar el texto y hacerlo más grande. Puedes probar otros estilos.

---

## 📐 Tipografía con MaterialTheme

**MaterialTheme** tiene una serie de estilos predefinidos para textos, tales como:

- **headlineSmall**, **headlineMedium**, **headlineLarge**: Tamaños para titulares.
- **labelSmall**, **labelMedium**, **labelLarge**: Tamaños para etiquetas.

Puedes usar estos estilos según el contexto de tu aplicación.

Por ejemplo, para un título principal podrías usar:

```kotlin
Text(
    text = "Conversor de Unidades",
    style = MaterialTheme.typography.headlineLarge
)
```
Esto hará que el título sea más grande y destacado.

---

## 🎨 Creando un Estilo de Texto Personalizado

Si deseas tener un estilo más específico, puedes crear un estilo personalizado usando **TextStyle**.

```kotlin
val customTextStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 32.sp,
    color = Color.Red
)
```

Nota: Seguramente tendrás que hacer muchas importaciones aquí.

Puedes colocar este valor justo después donde declaramos las variables de nuestro Composable y antes de la función para convertir las unidades.

Aunque lo puede sacar de la función.

Luego puedes aplicarlo a un componente **Text** de la siguiente manera:

```kotlin
Text(
    text = "Conversor de Unidades Personalizado",
    style = customTextStyle
)
```
Este ejemplo usa una fuente monoespaciada, con un tamaño de 32 SP y color rojo. Puedes experimentar con diferentes propiedades para obtener el estilo deseado.

---

## 📘 Recomendaciones sobre los estilos

[Hecho] No somos artistas ni diseñadores

- Siempre es buena idea seguir el lenguaje de diseño de **Material Theme** para mantener una apariencia consistente en tu aplicación.
- Puedes experimentar con distintos estilos y ver cuál se adapta mejor a la UX que quieres ofrecer.
- Es buena idea preguntar a una IA como `chatGPT` para que nos ayude con estas tareas de diseño.



## 🎓 Pro tip: Refactorizando el código 

¿Alguna vez has mirado tu código y has notado que algunas partes se repiten varias veces? Esto suele ser una buena señal para plantearse una refactorización. Repetir código puede dificultar la mantenibilidad, aumentar la probabilidad de errores y hacer que el código sea menos legible. Siguiendo las mejores filosofías de programación 💻, el refactorizar suele ser una excelente idea.

Piensa en tu código. ¿Crees que hay alguna parte que se está repitiendo? Para a pensarlo un momento.

En nuestro caso, estamos desarrollando un convertidor de unidades con dos `DropdownMenu` que contienen información muy similar. ¿Qué pasaría si quisiéramos añadir una nueva unidad? Con la versión que tenemos hasta ahora, habría que modificar cada `DropdownMenu` uno por uno, duplicando líneas de código. Esto sería un lío conforme el proyecto se haga más grande.

Vamos a ver una mejor solución que hace nuestro código más flexible y fácil de mantener ✍️:

### Paso 1: Crear una Data Class para modelar las unidades 🛠️
Primero definimos una `data class` llamada `UnitOption` para representar las unidades con un nombre y un factor de conversión.

<details>
  <summary>Pincha aquí para ver la definición de la data class.</summary>
<br>

```kotlin
    data class UnitOption(val name: String, val conversionFactor: Double)
```
</details>
<br>

### Paso 2: Crear la lista de unidades 🔢
Usamos `listOf` para crear una lista de `UnitOption` con las unidades que queremos manejar.

<details>
  <summary>Pincha aquí para ver la lista de unidades.</summary>
<br>

```kotlin
    val unitsList = listOf(
        UnitOption("Centímetros", 0.01),
        UnitOption("Metros", 1.0),
        UnitOption("Pies", 0.3048),
        UnitOption("Milímetros", 0.001)
    )
```
</details>
<br>

### Paso 3: Modificar el código para iterar sobre la lista ⏳
Finalmente, iteramos sobre `unitsList` en cada `DropdownMenu` para generar los elementos dinámicamente, en lugar de escribir cada `DropdownMenuItem` por separado. De esta manera, si necesitamos agregar o modificar una unidad, solo cambiamos la lista.

<details>
  <summary>Pincha aquí para ver la implementación de DropdownMenu.</summary>
<br>

```kotlin
    DropdownMenu(expanded = inputExpanded, onDismissRequest = { inputExpanded = false }) {
        unitsList.forEach { unit ->
            DropdownMenuItem(
                text = { Text(unit.name) },
                onClick = {
                    inputExpanded = false
                    inputUnit = unit.name
                    inputConvertionFactor = unit.conversionFactor
                    convertUnits()
                }
            )
        }
    }
```

OJO!! Esto es la solución para el primer DropdownMenu, tendrás que hacer lo mismo para el segundo DropdownMenu.

</details>
<br>

### Ventajas de esta solución 🚀
- **Fácil de mantener**: ¡Solo tienes que modificar la lista si añades una nueva unidad!
- **Legibilidad**: El código queda más limpio y sencillo de entender.
- **Reducción de errores**: Al no duplicar código, minimizamos el riesgo de cometer errores accidentales.

¿Qué opinas de esta refactorización? Seguro que te ahorras muchas líneas de código y haces tu proyecto más sostenible a largo plazo 🌟.

### Añade una nueva unidad 📏

¿Cuánto de difícil sería añadir la unidad yarda? Prueba a hacerlo!

Por cierto, 1 yarda es 0.9144 metros.