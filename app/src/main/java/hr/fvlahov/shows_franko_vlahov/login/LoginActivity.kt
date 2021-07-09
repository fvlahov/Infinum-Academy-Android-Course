package hr.fvlahov.shows_franko_vlahov.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.widget.addTextChangedListener
import hr.fvlahov.shows_franko_vlahov.welcome.WelcomeActivity
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val minEmailLength = 1
    private val minPasswordLength = 6

    private val emailErrorMessage = "Enter a valid email address!"

    private val emailRegex : Regex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex()

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
        binding.activityLoginIetEmail.addTextChangedListener { emailPasswordTextChangeListener() }
        binding.activityLoginIetPassword.addTextChangedListener { emailPasswordTextChangeListener() }

        //Shows navigation if email or password input is in focus and the keyboard is open
        binding.activityLoginIetEmail.setOnFocusChangeListener { _, hasFocus -> setNavigationVisibility(hasFocus) }
        binding.activityLoginIetPassword.setOnFocusChangeListener { _, hasFocus -> setNavigationVisibility(hasFocus) }
    }

    private fun emailPasswordTextChangeListener() {
        val emailLength = binding.activityLoginIetEmail.text?.length ?: 0
        val passwordLength = binding.activityLoginIetPassword.text?.length ?: 0

        val loginEnabled = emailLength >= minEmailLength && passwordLength >= minPasswordLength
        binding.activityLoginBtnLogin.isEnabled = loginEnabled
    }

    private fun initLoginButton() {
        binding.activityLoginBtnLogin.setOnClickListener {
            if (validateEmail()) {
                startWelcomeActivity()
            } else {
                showEmailErrorMessage()
            }
        }
    }

    private fun validateEmail(): Boolean {
        val target = binding.activityLoginIetEmail.text.toString()

        return emailRegex.matches(target)

        //Another method of validating email address -- Didn't test
        //return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private fun showEmailErrorMessage(){
        binding.activityLoginIlEmail.error = emailErrorMessage
    }

    private fun startWelcomeActivity(){
        val intent = WelcomeActivity.buildIntent(
                this,
                binding.activityLoginIetEmail.text.toString()
        )
        startActivity(intent)
    }
}