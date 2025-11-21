package es.uva.inf5g.psm.labo08

data class ShoppingItemModel(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)