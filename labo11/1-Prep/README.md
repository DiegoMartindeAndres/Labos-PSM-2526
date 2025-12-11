# 🛠️ Preparación del proyecto para trabajar con ROOM

## Crear un nuevo proyecto

1. **Selecciona** la opción de actividad vacía (**Empty Activity**)
2. **Asigna un nombre al proyecto**, por ejemplo: `Lista de Deseos`.
3. Recuerda que el `namespace` debe ser: `es.uva.inf5g.psm.` seguido del nombre de tu app

## Configurar el proyecto

> ⚠️ **Atención** ⚠️  
> 🚨 Esta es una parte complicada, asegúrate de seguir los pasos correctamente. Es muy fácil equivocarse aquí.  
> Además, por cada paso, es recomendable **compilar y ejecutar** para comprobar que nada se ha roto. 🚨

Vamos a cambiar la versión de Kotlin y vamos a agregar las dependencias necesarias. Usaremos las versiones más actuales en el momento de escribir este manual, con las versiones de este manual deberías poder compilar sin problemas, con versiones futuras más actualizadas no se puede garantizar que todo funcione correctamente.

Por cada pequeño paso, es recomendable sincronizar y ejecutar el proyecto para asegurarte de que todo está correcto. Y garantizar que no se ha roto nada.

### 1. Actualizar la versión de Kotlin

En el fichero `libs.versions.toml` actualiza la versión de Kotlin a la más reciente.
Busca la sección `[versions]` y modifica la versión de Kotlin.

¿Cómo puedo saber cual es la última versión de Kotlin?

En la web oficial de las [Releases de Kotlin](https://github.com/JetBrains/kotlin/releases)

```kotlin
[versions]
kotlin = "2.1.0"
```

Salva, sincroniza y ejecuta el proyecto. Se descargará la nueva versión de Kotlin y probablemente te saldrá un mensaje para buscar deprecaciones "Scan for deprecations" dile que sí, y no debería encontrar ningún problema, ya que no tenemos código aún.

### 2. Configuración con los catálogos de versiones de Gradle

En el archivo `libs.versions.toml`, elimina cualquier referencia al compilador Compose (aunque lo normal es que no lo tengas)
En la sección de plugins, añada la siguiente dependencia nueva:

```kotlin
[plugins]
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```
Salva, sincroniza y compila.

### 3. Añadir plugin en `build.gradle.kts (Module :app)`

En el archivo `build.gradle.kts (Module :app)` añade lo siguiente a la sección de plugins:

```kotlin
plugins {
   // Plugins que ya tengas
   alias(libs.plugins.compose.compiler)
}
```

Salva, sincroniza y compila.

### 4. Agregar dependencias necesarias

Una vez cargado el proyecto, debemos agregar las dependencias requeridas.

> ⚠️ **Atención** ⚠️  
> 🚨 Esta es una parte complicada, asegúrate de seguir los pasos correctamente. Es muy fácil equivocarse aquí.  
> Además, por cada paso, es recomendable **compilar y ejecutar** para comprobar que nada se ha roto. 🚨

Vamos a ir por partes, agregando las dependencias por este orden. Por cada paquete de dependencias, sincroniza, y ejecuta el proyecto para asegurarte de que todo está correcto.:

1. **Room Database**
2. **Navigation**
3. **Core KTX**
4. **Compose**

#### 4.1 Empezamos por agregar las dependencias de **Room Database**.

Debemos agregar las siguientes dependencias:

```kotlin
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1
androidx.room:room-compiler:2.6.1
```	

Sincroniza el proyecto y ejecútalo
> ⚠️ **Atención** ⚠️  
> 🚨 Dará un error!! 🚨

 

<details>
  <summary>El error tendrá la siguiente pinta.</summary>
<br>

```
Duplicate class org.intellij.lang.annotations.Identifier found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$AdjustableOrientation found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$BoxLayoutAxis found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$CalendarMonth found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$CursorType found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$FlowLayoutAlignment found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$FontStyle found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$HorizontalAlignment found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$InputEventMask found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$ListSelectionMode found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$PatternFlags found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$TabLayoutPolicy found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$TabPlacement found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$TitledBorderJustification found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$TitledBorderTitlePosition found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.JdkConstants$TreeSelectionMode found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.Language found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.MagicConstant found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.Pattern found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.PrintFormat found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.PrintFormatPattern found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.RegExp found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.intellij.lang.annotations.Subst found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.jetbrains.annotations.Nls found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.jetbrains.annotations.NonNls found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.jetbrains.annotations.NotNull found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.jetbrains.annotations.Nullable found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.jetbrains.annotations.PropertyKey found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
Duplicate class org.jetbrains.annotations.TestOnly found in modules annotations-12.0.jar -> annotations-12.0 (com.intellij:annotations:12.0) and annotations-23.0.0.jar -> annotations-23.0.0 (org.jetbrains:annotations:23.0.0)
```	

</details>
<br>

**¿Qué está ocurriendo?**

El árbol de dependencias en los proyectos con Kotlin y Jectpack Compose es muy complejo. En este caso, Room Database tiene una dependencia con `annotations-12.0` pero nosotros estamos usando la versión más actual que es: `annotations-23.0.0`. Cuando queremos compilar las dependencias de Room, se produce un conflicto entre las versiones de las dependencias.

**¿Cómo lo solucionamos?**

Vamos a excluir la dependencia `annotations-12.0` de Room Database. Y pasa ello en el fichero `build.gradle.kts (Module :app)`. Bucaremos en la sección de pendencias que tendremos algo como:

```kotlin
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.compiler)
```

Bien, pues por cada referencia diremos que queremos excluir la dependencia `annotations-12.0`:

```kotlin
    implementation(libs.androidx.room.runtime){
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.ktx){
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.compiler){
        exclude(group = "com.intellij", module = "annotations")
    }
```

Salva, sincroniza, compila y ejecuta.

#### 4.2 Añadimos las dependencias de **Core KTX**:

```kotlin
androidx.core:core-ktx:1.15.O
```	
Sincroniza el proyecto y ejecútalo.

#### 4.3 Añadimos las dependencias de **Navigation**:

```kotlin
androidx.navigation:navigation-compose:2.8.5
```	

Sincroniza el proyecto y ejecútalo.

#### 4.3 Añadimos las dependencias de **Compose**:

```kotlin
androidx.compose.material:material:1.7.6
androidx.compose.ui:ui:1.7.6
androidx.compose.ui:ui-tooling-preview:1.7.6
```

Sincroniza el proyecto y ejecútalo.

Si has llegado hasta aquí sin errores, ¡enhorabuena! Has configurado correctamente las dependencias. Pero aún queda la última parte!

### Añadir KSP (Kotlin Symbol Processing)

KSP es un procesador de anotaciones que se utiliza para generar código en tiempo de compilación. Es necesario para trabajar con Jetpack Compose y Room Database. Este procesador nos ayudará a trabajar con BBDD de una manera más sencilla.

En el fichero `build.gradle.kts (Module :app)` hay que hacer tres pasos más:

1. Añadir el plugin de KSP en la sección de plugins sin aplicar (para descargar las dependencias)
2. Eliminar que no aplique el plugin de KSP en la sección de plugins. Es decir, que aplique.
3. Añadir las dependencias de KSP.

Vamos a ir por partes:

#### 1. Añadir el plugin de KSP en la sección de plugins sin aplicar
En el fichero `build.gradle.kts (Module :app)` en la sección de plugins añade la siguiente línea. Se recomienda usar la última versión disponible.

¿Dónde busco la última versión de KSP? - En la [web oficial de KSP](https://github.com/google/ksp/releases)

```kotlin
plugins {
    // Otros plugins y alias que ya tengas
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false   
}
```
OJO!! es muy importante que sincronices y ejecutes.

#### 2. Eliminar que no aplique el plugin de KSP en la sección de plugins

Solo hay que quitar el `apply false` de la línea que acabamos de añadir. Debería quedar algo como:

```kotlin
plugins {
    // Otros plugins y alias que ya tengas
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}
```
Sincroniza y ejecuta!

#### 3. Añadir las dependencias de KSP

En el fichero `build.gradle.kts (Module :app)` localiza lo que ya escribimos.
Debes de tener algo como:

```kotlin
    implementation(libs.androidx.room.runtime){
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.ktx){
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.androidx.room.compiler){
        exclude(group = "com.intellij", module = "annotations")
    }
```

Al final de la dependencia `libs.androidx.room.compiler` añade la siguiente dependencia:

```kotlin
    ksp(libs.androidx.room.compiler)
```

Sincroniza el proyecto y ejecútalo.

¿Todo funciona correctamente? ¡Enhorabuena! Has configurado correctamente las dependencias y el proyecto. Esto ya es un +1 en la nota final del curso. XD

Ya podemos empezar a implementar nuestra aplicación de lista de deseos.