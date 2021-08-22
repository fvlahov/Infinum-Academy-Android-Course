package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.fvlahov.shows_franko_vlahov.core.ErrorType
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase

class ShowViewModelFactory(
    val database: ShowsDatabase,
    private val onShowsLoadCallback: () -> Unit,
    private val onErrorCallback: (errorType: ErrorType) -> Unit
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowViewModel::class.java)) {
            return ShowViewModel(
                database,
                onShowsLoadCallback,
                onErrorCallback
            ) as T
        }
        throw IllegalArgumentException("Class must be asignable from ShowViewModel!")
    }
}