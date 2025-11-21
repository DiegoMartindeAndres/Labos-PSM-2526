package es.uva.inf5g.psm.labo08

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ShoppingViewModel(private val repository: IShoppingRepository) : ViewModel() {
    private val _items = mutableStateOf(repository.getItems())
    val items: MutableState<List<ShoppingItemModel>> = _items

    fun addItem(name: String, quantity: Int) {
        val newItem = repository.createItem(name, quantity)
        repository.addItem(newItem)
        _items.value = repository.getItems()
    }


    fun removeItem(item: ShoppingItemModel) {
        repository.removeItem(item)
        _items.value = repository.getItems()
    }

    fun editItem(item: ShoppingItemModel, name: String, quantity: Int) {
        val updatedItem = item.copy(name = name, quantity = quantity, isEditing = false)
        repository.updateItem(updatedItem)
        _items.value = repository.getItems()
    }

    fun editItem(item: ShoppingItemModel){
        repository.editItem(item)
        _items.value = repository.getItems()
    }
}