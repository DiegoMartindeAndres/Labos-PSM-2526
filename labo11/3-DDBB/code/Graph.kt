package es.uva.inf5g.psm.listadedeseos

import android.content.Context
import androidx.room.Room
import es.uva.inf5g.psm.listadedeseos.data.WishDatabase
import es.uva.inf5g.psm.listadedeseos.data.WishRepository

object Graph {
    private lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context,
            WishDatabase::class.java,
            "wishlist.db"
        ).build()
    }
}