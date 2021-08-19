package hr.fvlahov.shows_franko_vlahov.core

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import hr.fvlahov.shows_franko_vlahov.R

open class BaseFragment : Fragment() {

    private lateinit var loadingProgressIndicator: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createProgressIndicator()
    }

    protected fun showLoadingIndicator(
        parent: ConstraintLayout,
        topToTopOfId: Int,
        bottomToBottomOfId: Int,
        startToStartOfId: Int,
        endToEndOfId: Int
    ) {
        parent.addView(loadingProgressIndicator)

        val set = ConstraintSet()
        set.clone(parent)
        set.connect(
            loadingProgressIndicator.id,
            ConstraintSet.END,
            endToEndOfId,
            ConstraintSet.END
        )
        set.connect(
            loadingProgressIndicator.id,
            ConstraintSet.TOP,
            topToTopOfId,
            ConstraintSet.TOP
        )
        set.connect(
            loadingProgressIndicator.id,
            ConstraintSet.BOTTOM,
            bottomToBottomOfId,
            ConstraintSet.BOTTOM
        )
        set.connect(
            loadingProgressIndicator.id,
            ConstraintSet.START,
            startToStartOfId,
            ConstraintSet.START
        )
        set.applyTo(parent)


        loadingProgressIndicator.isVisible = true
    }

    protected fun hideLoadingIndicator(parent: ConstraintLayout) {
        parent.removeView(loadingProgressIndicator)
        loadingProgressIndicator.isVisible = false
    }

    private fun createProgressIndicator() {
        loadingProgressIndicator = ProgressBar(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            elevation =
                context.resources.getDimensionPixelSize(R.dimen.circular_progress_elevation)
                    .toFloat()
        }
    }
}