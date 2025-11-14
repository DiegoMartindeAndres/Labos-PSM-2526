# 📘 Laboratorio 08: Transformación a MVVM de la Lista de la Compra

## 📝 Objetivo

En este laboratorio transformarás tu aplicación de la **Lista de la Compra** (desarrollada en los laboratorios 07 y 08) para que utilice el patrón arquitectónico **MVVM (Model–View–ViewModel)** con **estado observable** en Jetpack Compose.

## 🛠️ ¿Qué tienes que hacer?

1. **Crear un ViewModel** que gestione la lista de la compra (`ShoppingItem`), mantenga el estado con `MutableState` o `StateFlow` y exponga la lista para que la vista pueda observarla.
2. **Mover toda la lógica** de añadir, borrar o modificar elementos desde la UI al ViewModel.
3. **Actualizar tu pantalla** para que observe el estado del ViewModel y se recomponga automáticamente cuando cambie la lista.

📌 *En resumen:* debes **refactorizar tu aplicación actual del Lab 7 para que siga MVVM**, manteniendo el mismo comportamiento funcional, pero con una arquitectura limpia y separada por capas.

