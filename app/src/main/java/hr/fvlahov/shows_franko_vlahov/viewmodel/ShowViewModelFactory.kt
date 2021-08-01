package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase

class ShowViewModelFactory(val database: ShowsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowViewModel::class.java)) {
            return ShowViewModel(database) as T
        }
        throw IllegalArgumentException("Class must be asignable from ShowViewModel!")
    }
}