package hr.fvlahov.shows_franko_vlahov.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentRegisterBinding
import hr.fvlahov.shows_franko_vlahov.login.MIN_EMAIL_LENGTH
import hr.fvlahov.shows_franko_vlahov.login.MIN_PASSWORD_LENGTH
import hr.fvlahov.shows_franko_vlahov.utils.NavigationHelper
import hr.fvlahov.shows_franko_vlahov.utils.ValidationHelper


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private var emailErrorMessage = ""
    private var passwordErrorMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        NavigationHelper().setNavigationVisibility(activity, false)
        initRegisterButton()
        initInputs()
        return binding.root
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
        TODO("Not yet implemented")
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
        if (binding.inputPassword.text != binding.inputConfirmPassword.text) {
            binding.containerConfirmPassword.error = resources.getString(R.string.passwords_dont_match)
            return false
        }
        return true
    }
}