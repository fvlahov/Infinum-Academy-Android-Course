package hr.fvlahov.shows_franko_vlahov.preferences

import android.content.Context
import android.content.SharedPreferences
import hr.fvlahov.shows_franko_vlahov.login.USER_EMAIL
import hr.fvlahov.shows_franko_vlahov.login.USER_ID
import hr.fvlahov.shows_franko_vlahov.login.USER_IMAGE
import hr.fvlahov.shows_franko_vlahov.model.api_response.User
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule

class PreferenceHelper(private val preferences: SharedPreferences?) {

    fun saveLoginData(accessToken: String?, client: String?, uid: String?) {
        preferences?.edit()?.apply {
            putString(
                ApiModule.ACCESS_TOKEN,
                accessToken
            )
            putString(
                ApiModule.CLIENT,
                client
            )
            putString(
                ApiModule.UID,
                uid
            )
            apply()
        }
    }

    fun saveCurrentUser(user: User?) {
        if(user != null){
            with(preferences?.edit()) {
                this?.putInt(USER_ID, user.id)
                this?.putString(USER_EMAIL, user.email)
                this?.putString(USER_IMAGE, user.imageUrl)
                this?.apply()
            }
        }
    }

    fun saveCurrentUser(id: Int, email: String, imageUrl: String?) {
        with(preferences?.edit()) {
            this?.putInt(USER_ID, id)
            this?.putString(USER_EMAIL, email)
            this?.putString(USER_IMAGE, imageUrl)
            this?.apply()
        }
    }

    fun getCurrentUser(): User =
        User(
            id = preferences?.getInt(USER_ID, 0) ?: 0,
            email = preferences?.getString(USER_EMAIL, "user") ?: "user",
            imageUrl = preferences?.getString(USER_IMAGE, null)
        )
}