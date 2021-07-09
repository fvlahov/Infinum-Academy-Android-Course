package hr.fvlahov.shows_franko_vlahov.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import hr.fvlahov.shows_franko_vlahov.main.MainActivity
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loginEnabled: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root = binding.root

        setContentView(root)

        initLoginButton()
        initInputs()
    }

    private fun initInputs() {
        binding.ietEmail.addTextChangedListener { emailPasswordTextChangeListener() }
        binding.ietPassword.addTextChangedListener { emailPasswordTextChangeListener() }
    }

    private fun emailPasswordTextChangeListener() {
        loginEnabled = binding.ietEmail.text?.isNotEmpty() ?: false && binding.ietPassword.text?.isNotEmpty() ?: false
        binding.btnLogin.isEnabled = loginEnabled
    }

    private fun initLoginButton() {
        binding.btnLogin.setOnClickListener {
            val intent = MainActivity.buildIntent(
                    this,
                    binding.ietEmail.text.toString()
            )
            startActivity(intent)
        }
    }
}