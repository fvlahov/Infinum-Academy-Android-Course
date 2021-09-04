package hr.fvlahov.shows_franko_vlahov.splash

import android.animation.TimeInterpolator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.navigation.fragment.findNavController
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentShowsBinding
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        animateImageTriangle()
    }

    private fun animateImageTriangle() {

        with(binding.imageTriangle) {
            translationY = -2000f
            animate()
                .translationY(0f)
                .setDuration(1500)
                .setInterpolator(BounceInterpolator())
                .withEndAction {
                    animateLabelShows()
                }
                .start()
        }
    }

    private fun animateLabelShows() {

        with(binding.labelShows) {
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(OvershootInterpolator())
                .setDuration(1000)
                .withEndAction {
                    navigateToLogin()
                }
                .start()
        }
    }

    private fun navigateToLogin() {

        //Wait 2 seconds without blocking the main thread, then navigate onwards
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            findNavController().navigate(R.id.action_splash_to_login)
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}