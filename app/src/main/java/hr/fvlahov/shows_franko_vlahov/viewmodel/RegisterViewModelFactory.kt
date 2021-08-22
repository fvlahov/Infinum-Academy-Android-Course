package hr.fvlahov.shows_franko_vlahov.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.fvlahov.shows_franko_vlahov.preferences.PreferenceHelper

class RegisterViewModelFactory(
    private val onRegisterResponse: () -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(onRegisterResponse) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}