package es.uva.inf5g.psm.labo08

import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.maxOfOrNull
import kotlin.collections.plus

class ShoppingRepository: IShoppingRepository{
    private var _items = listOf(
        ShoppingItemModel(1, "Unicornio Inflable", 1),
        ShoppingItemModel(2, "Sombrero para Gatos", 3),
        ShoppingItemModel(3, "Bocadillo de Aire Fresco", 1)
    )

    override fun getItems(): List<ShoppingItemModel> = _items

    override fun createItem(name: String, quantity: Int): ShoppingItemModel {
        val newId = (_items.maxOfOrNull { it.id } ?: 0) + 1
        return ShoppingItemModel(newId, name, quantity)
    }

    override fun addItem(item: ShoppingItemModel) {
        _items = _items + item
    }

    override fun removeItem(item: ShoppingItemModel) {
        _items = _items.filter { it.id != item.id }
    }

    override fun updateItem(updatedItem: ShoppingItemModel) {
        _items = _items.map { if (it.id == updatedItem.id) updatedItem else it }
    }

    override fun editItem(itemToEdit: ShoppingItemModel) {
        _items = _items.map { item ->
            if (item.id == itemToEdit.id)
                item.copy(isEditing = true)
            else
                item
        }
    }
}