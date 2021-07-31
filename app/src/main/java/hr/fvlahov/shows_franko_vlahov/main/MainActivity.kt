package hr.fvlahov.shows_franko_vlahov.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.fvlahov.shows_franko_vlahov.database.ShowsDatabase
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityMainBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.ListShowsResponse
import hr.fvlahov.shows_franko_vlahov.networking.ApiModule
import hr.fvlahov.shows_franko_vlahov.utils.NetworkChecker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    val showsDatabase by lazy {
        ShowsDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ApiModule.initRetrofit(getPreferences(Context.MODE_PRIVATE))

    }
}