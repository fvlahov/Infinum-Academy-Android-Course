package hr.fvlahov.shows_franko_vlahov.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentLoginBinding
import hr.fvlahov.shows_franko_vlahov.preferences.PreferenceHelper
import hr.fvlahov.shows_franko_vlahov.register.RegisterFragmentDirections
import hr.fvlahov.shows_franko_vlahov.utils.NavigationHelper
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import hr.fvlahov.shows_franko_vlahov.utils.ValidationHelper
import hr.fvlahov.shows_franko_vlahov.viewmodel.LoginViewModel
import hr.fvlahov.shows_franko_vlahov.viewmodel.LoginViewModelFactory
import java.util.concurrent.Executors

const val REMEMBER_ME_LOGIN = "rememberMeLogin"
const val USER_EMAIL = "userEmail"
const val USER_IMAGE = "userImage"
const val USER_ID = "userId"
const val MIN_EMAIL_LENGTH = 1
const val MIN_PASSWORD_LENGTH = 6

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    private val args: LoginFragmentArgs by navArgs()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val shouldNavigateToShows = prefs?.getBoolean(REMEMBER_ME_LOGIN, false)
        if (shouldNavigateToShows == true) {
            navigateToShows()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModelFactory =
            LoginViewModelFactory(PreferenceHelper(activity?.getPreferences(Activity.MODE_PRIVATE)))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(LoginViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NavigationHelper().setNavigationVisibility(activity, false)
        updateViewsIfRegistrationSuccessful(args.registerSuccessful)
        initLoginButton()
        initInputs()

        viewModel.getLoginResultLiveData()
            .observe(viewLifecycleOwner) { isLoginSuccessful ->
                if (isLoginSuccessful) {
                    rememberMeOnLogin(binding.checkboxRememberMe.isChecked)
                    navigateToShows()
                } else {
                    binding.containerEmail.error = getString(R.string.email_password_dont_match)
                    binding.containerPassword.error = getString(R.string.email_password_dont_match)
                }
            }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.fragment_register)
        }
    }

    private fun updateViewsIfRegistrationSuccessful(registerSuccessful: Boolean) {
        if (registerSuccessful) {
            binding.labelLogin.text = resources.getString(R.string.registration_successful)
            binding.buttonRegister.isVisible = false
        }
    }

    private fun initInputs() {
        binding.inputEmail.addTextChangedListener { onEmailPasswordTextChanged() }
        binding.inputPassword.addTextChangedListener { onEmailPasswordTextChanged() }

        val navHelper = NavigationHelper()

        //Shows navigation if email or password input is in focus which means the keyboard is open
        binding.inputEmail.setOnFocusChangeListener { _, hasFocus ->
            navHelper.setNavigationVisibility(
                activity,
                hasFocus
            )
        }
        binding.inputPassword.setOnFocusChangeListener { _, hasFocus ->
            navHelper.setNavigationVisibility(
                activity,
                hasFocus
            )
        }
    }

    private fun onEmailPasswordTextChanged() {
        val emailLength = binding.inputEmail.text?.length ?: 0
        val passwordLength = binding.inputPassword.text?.length ?: 0

        val loginEnabled = emailLength >= MIN_EMAIL_LENGTH && passwordLength >= MIN_PASSWORD_LENGTH
        binding.buttonLogin.isEnabled = loginEnabled
    }

    private fun initLoginButton() {
        binding.buttonLogin.setOnClickListener {
            if (ValidationHelper().validateEmail(binding.inputEmail.text.toString())) {
                attemptLogin()
            } else {
                showEmailErrorMessage(resources.getString(R.string.enter_valid_email))
            }
        }
    }

    private fun showEmailErrorMessage(message: String) {
        binding.containerEmail.error = message
    }

    private fun attemptLogin() {
        Executors.newSingleThreadExecutor().execute {
            if (NetworkChecker().checkInternetConnectivity()) {
                viewModel.login(
                    binding.inputEmail.text.toString(),
                    binding.inputPassword.text.toString()
                )
            } else {
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    resources.getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }


    }

    private fun saveUserEmail() {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()) {
            this?.putString(USER_EMAIL, binding.inputEmail.text.toString())
            this?.apply()
        }
    }

    private fun rememberMeOnLogin(shouldRemember: Boolean) {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()) {
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