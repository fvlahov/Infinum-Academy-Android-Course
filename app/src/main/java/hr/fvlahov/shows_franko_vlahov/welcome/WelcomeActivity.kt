package hr.fvlahov.shows_franko_vlahov.welcome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    companion object{
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun buildIntent(activity: Activity, email: String) : Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }

    private lateinit var binding : ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        val email = intent.extras?.getString(EXTRA_EMAIL) ?: "User"
        val name = email.substringBefore('@')
        val welcomeText = binding.activityWelcomeTvWelcome.text.toString().plus(" ").plus(name).plus("!")

        binding.activityWelcomeTvWelcome.text = welcomeText
    }
}