package hr.fvlahov.shows_franko_vlahov.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityShowsBinding
import hr.fvlahov.shows_franko_vlahov.model.Show

class ShowsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(context: Activity): Intent {
            return Intent(context, ShowsActivity::class.java)
        }
    }

    private val shows = listOf(
        Show("office", "The Office", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_office),
        Show("strangerThings", "Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("bloodAintWater", "Krv nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_krv_nije_voda)
    )

    private var showsVisibility = false

    private lateinit var binding: ActivityShowsBinding
    private var adapter: ShowsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        initShowsRecyclerView()
        initShowHideEmptyStateButton()

        setContentView(binding.root)
    }

    private fun initShowHideEmptyStateButton() {
        binding.buttonShowHideEmptyState.setOnClickListener {
            if(showsVisibility){
                binding.imageEmptyShows.visibility = View.GONE
                binding.labelEmptyShows.visibility = View.GONE
                binding.recyclerShows.visibility = View.VISIBLE
            }
            else{
                binding.imageEmptyShows.visibility = View.VISIBLE
                binding.labelEmptyShows.visibility = View.VISIBLE
                binding.recyclerShows.visibility = View.GONE
            }
            showsVisibility = !showsVisibility
        }
    }

    private fun initShowsRecyclerView() {
        binding.recyclerShows.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ShowsAdapter(shows){ show ->
            onShowClicked(show)
        }
        binding.recyclerShows.adapter = adapter

        if(adapter?.itemCount ?: 0 < 1){
            binding.recyclerShows.visibility = View.GONE
        }
        else{
            binding.imageEmptyShows.visibility = View.GONE
            binding.labelEmptyShows.visibility = View.GONE
        }
    }

    private fun onShowClicked(show: Show) {

    }
}