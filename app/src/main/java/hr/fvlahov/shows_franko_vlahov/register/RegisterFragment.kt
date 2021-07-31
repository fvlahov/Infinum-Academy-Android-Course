package hr.fvlahov.shows_franko_vlahov.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentRegisterBinding
import hr.fvlahov.shows_franko_vlahov.login.LoginFragmentDirections
import hr.fvlahov.shows_franko_vlahov.login.MIN_EMAIL_LENGTH
import hr.fvlahov.shows_franko_vlahov.login.MIN_PASSWORD_LENGTH
import hr.fvlahov.shows_franko_vlahov.shows.ShowsFragmentDirections
import hr.fvlahov.shows_franko_vlahov.utils.NavigationHelper
import hr.fvlahov.shows_franko_vlahov.utils.ValidationHelper
import hr.fvlahov.shows_franko_vlahov.viewmodel.RegisterViewModel


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    private var emailErrorMessage = ""
    private var passwordErrorMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        NavigationHelper().setNavigationVisibility(activity, false)
        initRegisterButton()
        initInputs()

        initViewModel()

        return binding.root
    }

    private fun initViewModel() {
        viewModel.getRegistrationResultLiveData()
            .observe(this.viewLifecycleOwner) { isRegisterSuccessful ->
                if (isRegisterSuccessful) {
                    val action = RegisterFragmentDirections.actionRegisterToLogin()
                    action.registerSuccessful = true
                    findNavController().navigate(action)
                } else {
                    binding.containerEmail.error = getString(R.string.user_exists)
                }
            }
    }

    private fun initRegisterButton() {
        binding.buttonRegister.setOnClickListener {
            if (validateInputsAndHandleError()) {
                attemptRegister()
            }
        }
    }

    private fun initInputs() {
        binding.inputEmail.addTextChangedListener { onEmailPasswordConfirmPasswordTextChanged() }
        binding.inputPassword.addTextChangedListener { onEmailPasswordConfirmPasswordTextChanged() }
        binding.inputConfirmPassword.addTextChangedListener { onEmailPasswordConfirmPasswordTextChanged() }


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
        binding.inputConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            navHelper.setNavigationVisibility(
                activity,
                hasFocus
            )
        }
    }

    private fun onEmailPasswordConfirmPasswordTextChanged() {
        val emailLength = binding.inputEmail.text?.length ?: 0
        val passwordLength = binding.inputPassword.text?.length ?: 0

        val registerEnabled =
            emailLength >= MIN_EMAIL_LENGTH && passwordLength >= MIN_PASSWORD_LENGTH

        binding.buttonRegister.isEnabled = registerEnabled
    }

    private fun attemptRegister() {
        binding.apply {
            viewModel.register(
                inputEmail.text.toString(),
                inputPassword.text.toString(),
                inputConfirmPassword.text.toString()
            )
        }
    }

    private fun validateInputsAndHandleError(): Boolean {
        val validatorHelper = ValidationHelper()

        binding.containerEmail.error = ""
        binding.containerPassword.error = ""
        binding.containerConfirmPassword.error = ""

        if (!validatorHelper.validateEmail(binding.inputEmail.text.toString())) {
            binding.containerEmail.error = resources.getString(R.string.enter_valid_email)
            return false
        }
        if (binding.inputPassword.text.toString() != binding.inputConfirmPassword.text.toString()) {
            binding.containerConfirmPassword.error =
                resources.getString(R.string.passwords_dont_match)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}