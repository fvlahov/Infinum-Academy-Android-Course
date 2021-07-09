package hr.fvlahov.shows_franko_vlahov.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding

class WelcomeActivity : AppCompatActivity() {

    companion object{
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun buildIntent(activity: Activity, email: String) : Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        val email = intent.extras?.getString(EXTRA_EMAIL).plus("!")

        binding.tvWelcome.text = binding.tvWelcome.text.toString().plus(email)
    }
}