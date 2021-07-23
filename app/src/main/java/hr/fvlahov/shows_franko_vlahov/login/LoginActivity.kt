package hr.fvlahov.shows_franko_vlahov.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityLoginBinding
import hr.fvlahov.shows_franko_vlahov.shows.ShowsActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val minEmailLength = 1
    private val minPasswordLength = 6

    private val emailInvalidMessage = "Enter a valid email address!"
    private val emailNotRecognizedMessage = "Email not recognized!"
    private val emailPasswordCombinationErrorMessage =
        "Email and password combination not recognized!"

    //Regular expression for email validation -> any word + '@' + any word + match between 2 and 4 of the preceeding token
    private val emailRegex: Regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

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
    private fun setNavigationVisibility(navigationVisibility: Boolean) {
        if (navigationVisibility) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    private fun initInputs() {
        binding.inputEmail.addTextChangedListener { onEmailPasswordTextChanged() }
        binding.inputPassword.addTextChangedListener { onEmailPasswordTextChanged() }

        //Shows navigation if email or password input is in focus which means the keyboard is open
        binding.inputEmail.setOnFocusChangeListener { _, hasFocus ->
            setNavigationVisibility(
                hasFocus
            )
        }
        binding.inputPassword.setOnFocusChangeListener { _, hasFocus ->
            setNavigationVisibility(
                hasFocus
            )
        }
    }

    private fun onEmailPasswordTextChanged() {
        val emailLength = binding.inputEmail.text?.length ?: 0
        val passwordLength = binding.inputPassword.text?.length ?: 0

        val loginEnabled = emailLength >= minEmailLength && passwordLength >= minPasswordLength
        binding.buttonLogin.isEnabled = loginEnabled
    }

    private fun initLoginButton() {
        binding.buttonLogin.setOnClickListener {
            if (validateEmail()) {
                attemptLogin()
            } else {
                showEmailErrorMessage(emailInvalidMessage)
            }
        }
    }

    private fun validateEmail(): Boolean {
        val target = binding.inputEmail.text.toString()

        return emailRegex.matches(target)

        //Another method of validating email address -- Didn't test
        //return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private fun showEmailErrorMessage(message: String) {
        binding.containerEmail.error = message
    }

    private fun attemptLogin() {
        //TODO: Check email and password
        val canLogin = true
        if (canLogin) {
            startShowsActivity()
        } else {
            //TODO: Show appropriate error message
        }
    }

    private fun startShowsActivity() {
        val intent = ShowsActivity.buildIntent(
            this
        )
        startActivity(intent)
    }
}