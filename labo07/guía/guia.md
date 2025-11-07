# 🧭 Guía para la mini-explicación previa (10–15 min)

> **Meta del lab**: construir una **lista de la compra** en Jetpack Compose con **añadir, listar, editar y borrar** usando estado y componibles reutilizables.

---

## 1) Orden del recorrido (lo que voy a construir en directo)
1. **Estructura base**: `Scaffold` → `Column` centrada → **Botón “Añadir”**.
2. **Lista**: introducir **`LazyColumn`** bajo el botón.
3. **Modelo**: `data class ShoppingItem(id, name, quantity, isEditing)`.
4. **Estado**: `var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }`.
5. **Diálogo**: `AlertDialog` con `showDialog`, dos `OutlinedTextField` (nombre/cantidad).
6. **Alta**: al confirmar, crear ítem y **`shoppingItems = shoppingItems + newItem`**.
7. **Item UI**: componible `ShoppingListItem(item, onEditClick, onDeleteClick)` con borde y botones.
8. **Edición**: `ShoppingItemEditor(item, onEditComplete)` + conmutar `isEditing` con `map/copy`.
9. **Eliminar**: `shoppingItems = shoppingItems - item`.

---

## 2) Conceptos de Compose que deben quedar claros
- **Componibles**: funciones `@Composable` pequeñas y reutilizables.
- **`Modifier`**: compone comportamiento/estilo (**`fillMaxWidth`**, **`padding`**, **`weight(1f)`**).
- **`remember`/`mutableStateOf`**: el **estado** vive en la UI y **dispara recomposición**.
- **`LazyColumn` “perezosa”**: solo renderiza lo visible; usar `items(lista)`.

---

## 3) Estado y listas (patrones prácticos)
- **Añadir** (inmutabilidad de listas):
  ```kotlin
  if (itemName.isNotBlank()) {
      val qty = itemQuantity.toIntOrNull() ?: 1
      val newItem = ShoppingItem(id = shoppingItems.size + 1, name = itemName, quantity = qty)
      shoppingItems = shoppingItems + newItem
  }
```

* **Editar** (activar editor de un item):

  ```kotlin
  shoppingItems = shoppingItems.map { it.copy(isEditing = it.id == item.id) }
  ```
* **Aplicar cambios** (patrón `map/copy/let`):

  ```kotlin
  val edited = shoppingItems.find { it.id == item.id }
  edited?.let { e ->
      val updated = e.copy(name = editedName, quantity = editedQty, isEditing = false)
      shoppingItems = shoppingItems.map { if (it.id == updated.id) updated else it }
  }
  ```
* **Eliminar**:

  ```kotlin
  shoppingItems = shoppingItems - item
  ```

---

## 4) Diálogo de alta (`AlertDialog`)

* **Control de visibilidad**: `var showDialog by remember { mutableStateOf(false) }`.
* **Campos**: dos `OutlinedTextField` (nombre y cantidad).
* **Confirmación**: validar nombre **no vacío** y cantidad con `toIntOrNull() ?: 1`.
* **Solo un “Cancelar”**: si uso botón propio, quitar `dismissButton` para no duplicar.

---

## 5) Componibles reutilizables

* **`ShoppingListItem`**

  * Layout: `Row` con **`Column.weight(1f)`** (nombre/cantidad) + `Row` de **IconButtons** (✏️/🗑️).
  * Estilo: `border(BorderStroke(...), RoundedCornerShape(20))`.
* **`ShoppingItemEditor`**

  * Dos campos editables + botón **Guardar** que llama `onEditComplete(name, qty)`.

---

## 6) Alineación y diseño (tips rápidos)

* **Botones a la derecha**: poner los textos en `Column(modifier = Modifier.weight(1f))`.
* **Separación**: `padding(8.dp)` en contenedores e inputs.
* **Accesibilidad**: `contentDescription` en `Icon`.

---

## 7) Preguntas relámpago para comprobar comprensión

* ¿Por qué `LazyColumn` y no una `Column`?
* Diferencia entre **propiedades del componible** y **`Modifier`**.
* ¿Qué provoca la recomposición al añadir/editar?
* ¿Por qué usamos **`map` + `copy`** en lugar de mutar directamente?

---

## 8) Errores típicos que quiero evitar (y cómo)

* **`NumberFormatException`**: usar `toIntOrNull() ?: 1`.
* **Duplicar “Cancelar”** en `AlertDialog`: si hay `confirmButton` propio, **eliminar** `dismissButton`.
* **IDs** con `size + 1` → vale para el lab, pero **no es robusto** tras borrar (mencionarlo).
* **Estado en el item**: cuidado con mezclar `var` mutables en la `data class`; el patrón recomendado es **crear copias**.
* **Lista mutable**: no mutar in-place; **reasignar** (`items = items + ...`, `items = items.map { ... }`).

---

## 9) Checkpoints visuales (lo que deberíais ver)

1. **Solo botón** centrado.
2. **Botón + LazyColumn** (vacía primero, luego con mocks).
3. **Dialog** con dos campos.
4. **Lista con ítems** mostrando **nombre/cantidad**.
5. **Editar/Guardar** funciona; **Eliminar** funciona.

---

## 10) Si sobra tiempo (extensiones sugeridas)

* `rememberSaveable` para rotaciones.
* Validación de entrada (cantidad > 0).
* `KeyboardOptions(keyboardType = KeyboardType.Number)`.
* Marcar “comprado” con `Checkbox` y estilo tachado.
* `LazyColumn(items, key = { it.id })` para estabilidad.

---

