package hr.fvlahov.shows_franko_vlahov.utils

import android.app.Activity
import android.view.View

class NavigationHelper {
    fun setNavigationVisibility(activity: Activity?, navigationVisibility: Boolean) {
        if (navigationVisibility) {
            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}