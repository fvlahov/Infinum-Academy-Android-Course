package hr.fvlahov.shows_franko_vlahov.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.findFragment
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ApiModule.initRetrofit(getPreferences(Context.MODE_PRIVATE))
    }
}