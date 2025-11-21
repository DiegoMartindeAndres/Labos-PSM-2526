package es.uva.inf5g.psm.labo08

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.jvm.java

/**
 * Factory personalizada para crear instancias de ShoppingViewModel.
 *
 * Android normalmente crea los ViewModel utilizando un constructor vacío.
 * Cuando un ViewModel tiene parámetros en su constructor —como un repositorio,
 * DAO u otra dependencia— el sistema no sabe cómo crearlo y lanza una excepción:
 *
 * Para solucionar esto, se utiliza un ViewModelProvider.Factory.
 * Esta clase indica explícitamente cómo debe construirse el ViewModel
 * cuando necesita dependencias externas. De este modo:
 *   -Permite pasar el repositorio al ViewModel sin romper el ciclo de vida.
 *   -Evita crashes por falta de constructor vacío.
 *   -Mantiene el ViewModel independiente de la creación de sus dependencias.
 *   -Facilita las pruebas (testability) y una arquitectura más limpia.
 *
 * En resumen: el Factory actúa como “constructor personalizado”
 * para ViewModels que requieren parámetros.
 */
class ShoppingViewModelFactory(
    private val repository: IShoppingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingViewModel(repository) as T
        }
        throw kotlin.IllegalArgumentException("Unknown ViewModel class")
    }
}
