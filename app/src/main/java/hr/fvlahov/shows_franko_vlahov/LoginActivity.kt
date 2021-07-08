package hr.fvlahov.shows_franko_vlahov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityLoginBinding
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root = binding.root

        setContentView(root)

        initLoginButton()
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