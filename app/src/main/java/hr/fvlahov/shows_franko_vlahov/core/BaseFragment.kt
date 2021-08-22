package hr.fvlahov.shows_franko_vlahov.core

import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import hr.fvlahov.shows_franko_vlahov.R

open class BaseFragment : Fragment() {

    protected fun showErrorDialog(errorType: ErrorType, onOkClicked: () -> Unit = {}) {
        Handler(Looper.getMainLooper()).post {

            var title = resources.getString(R.string.error)
            val message = when (errorType) {
                ErrorType.API -> resources.getString(R.string.api_error)
                ErrorType.DATABASE -> resources.getString(R.string.api_error)
                ErrorType.OTHER -> resources.getString(R.string.internal_error)
                ErrorType.NO_INTERNET -> resources.getString(R.string.no_internet)
            }

            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }

            builder?.apply {
                setMessage(message)
                setTitle(title)
                setPositiveButton(
                    R.string.ok
                ) { dialog, id -> onOkClicked() }
            }


            val dialog: AlertDialog? = builder?.create()
            dialog?.show()
        }

    }
}

