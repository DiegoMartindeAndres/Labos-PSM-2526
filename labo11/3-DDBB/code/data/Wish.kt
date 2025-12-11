package es.uva.inf5g.psm.listadedeseos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="wish-table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="wish-title")
    val title: String="",
    @ColumnInfo(name="wish-desc")
    val description: String=""
)


object DummyWish {
    val wishList = listOf(
        Wish(
            title = "Batidora galáctica 3000",
            description = "Prepara smoothies que te llevarán a otra dimensión 🚀"
        ),
        Wish(
            title = "Auriculares antimolestias",
            description = "¡Apaga al mundo y enciende tu música! 🎧"
        ),
        Wish(
            title = "Silla gamer super pro",
            description = "Porque sentarse como un profesional también cuenta 🕹️"
        ),
        Wish(
            title = "Curso de Llados",
            description = "Hazte unos burpees y baja esa fucking panza 💪"
        )
    )
}