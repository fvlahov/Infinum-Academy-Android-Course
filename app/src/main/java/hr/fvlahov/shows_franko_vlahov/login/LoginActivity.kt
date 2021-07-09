package hr.fvlahov.shows_franko_vlahov.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import hr.fvlahov.shows_franko_vlahov.main.WelcomeActivity
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loginEnabled: Boolean = false;

    private val minEmailLength = 1
    private val minPasswordLength = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root = binding.root

        setContentView(root)

        setNavigationVisibility(false)
        initLoginButton()
        initInputs()
    }

    // Deprecated, but couldn't find any other way to hide navigation and notification drawer
    private fun setNavigationVisibility(newVisible: Boolean) {
        if (newVisible) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    private fun initInputs() {
        binding.ietEmail.addTextChangedListener { emailPasswordTextChangeListener() }
        binding.ietPassword.addTextChangedListener { emailPasswordTextChangeListener() }
        binding.ietEmail.setOnFocusChangeListener { v, hasFocus -> setNavigationVisibility(hasFocus) }
        binding.ietPassword.setOnFocusChangeListener { v, hasFocus -> setNavigationVisibility(hasFocus) }
    }

    private fun emailPasswordTextChangeListener() {
        val emailLength = binding.ietEmail.text?.length ?: 0
        val passwordLength = binding.ietPassword.text?.length ?: 0

        loginEnabled = emailLength >= minEmailLength  &&  passwordLength >= minPasswordLength
        binding.btnLogin.isEnabled = loginEnabled
    }

    private fun initLoginButton() {
        binding.btnLogin.setOnClickListener {
            if (validateEmail()) {
                val intent = WelcomeActivity.buildIntent(
                        this,
                        binding.ietEmail.text.toString()
                )
                startActivity(intent)
            } else {
                //TODO: Show error
            }
        }
    }

    private fun validateEmail(): Boolean {

    }
}