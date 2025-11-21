package es.uva.inf5g.psm.labo08

interface IShoppingRepository {

    fun getItems(): List<ShoppingItemModel>

    fun createItem(name: String, quantity: Int): ShoppingItemModel

    fun addItem(item: ShoppingItemModel)

    fun removeItem(item: ShoppingItemModel)

    fun updateItem(updatedItem: ShoppingItemModel)

    fun editItem(itemToEdit: ShoppingItemModel)
}
