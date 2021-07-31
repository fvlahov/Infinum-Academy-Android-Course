package hr.fvlahov.shows_franko_vlahov.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentLoginBinding
import hr.fvlahov.shows_franko_vlahov.shows.ShowsFragmentDirections

const val REMEMBER_ME_LOGIN = "rememberMeLogin"
const val USER_EMAIL = "userEmail"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val minEmailLength = 1
    private val minPasswordLength = 6

    private val emailInvalidMessage = "Enter a valid email address!"
    private val emailNotRecognizedMessage = "Email not recognized!"
    private val emailPasswordCombinationErrorMessage =
        "Email and password combination not recognized!"

    //Regular expression for email validation -> any word + '@' + any word + match between 2 and 4 of the preceeding token
    private val emailRegex: Regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setNavigationVisibility(false)
        initLoginButton()
        initInputs()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val shouldNavigateToShows = prefs?.getBoolean(REMEMBER_ME_LOGIN, false)
        if(shouldNavigateToShows == true){
            navigateToShows()
        }
    }

    // Deprecated, but couldn't find any other way to hide navigation and notification drawer
    private fun setNavigationVisibility(navigationVisibility: Boolean) {
        if (navigationVisibility) {
            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
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
            rememberMeOnLogin(binding.checkboxRememberMe.isChecked)
            saveUserEmail()
            navigateToShows()
        } else {
            //TODO: Show appropriate error message
        }
    }

    private fun saveUserEmail() {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()){
            this?.putString(USER_EMAIL, binding.inputEmail.text.toString())
            this?.apply()
        }
    }

    private fun rememberMeOnLogin(shouldRemember: Boolean) {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()){
            this?.putBoolean(REMEMBER_ME_LOGIN, shouldRemember)
            this?.apply()
        }
    }

    private fun navigateToShows() {
        findNavController().navigate(R.id.action_login_to_shows)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}