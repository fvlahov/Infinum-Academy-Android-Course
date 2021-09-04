package hr.fvlahov.shows_franko_vlahov.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.fvlahov.shows_franko_vlahov.preferences.PreferenceHelper

class LoginViewModelFactory(
    private val preferenceHelper: PreferenceHelper,
    private val onLoginResponse: () -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(preferenceHelper, onLoginResponse) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}