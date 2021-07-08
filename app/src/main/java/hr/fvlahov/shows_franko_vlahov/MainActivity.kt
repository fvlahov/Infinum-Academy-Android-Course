package hr.fvlahov.shows_franko_vlahov

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun buildIntent(activity: Activity, email: String) : Intent {
            val intent = Intent(activity, MainActivity::class.java)
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
    }
}