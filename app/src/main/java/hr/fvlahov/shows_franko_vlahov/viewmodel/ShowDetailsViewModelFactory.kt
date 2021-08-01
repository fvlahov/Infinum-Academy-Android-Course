package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase

class ShowDetailsViewModelFactory(val database: ShowsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(database) as T
        }
        throw IllegalArgumentException("Class must be asignable from ShowDetailsViewModel!")
    }
}