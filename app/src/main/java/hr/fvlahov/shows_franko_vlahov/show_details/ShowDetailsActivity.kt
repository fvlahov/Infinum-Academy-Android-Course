package hr.fvlahov.shows_franko_vlahov.show_details

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityShowDetailsBinding
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityShowsBinding
import hr.fvlahov.shows_franko_vlahov.model.Show
import hr.fvlahov.shows_franko_vlahov.shows.ReviewsAdapter
import hr.fvlahov.shows_franko_vlahov.shows.ShowsActivity
import hr.fvlahov.shows_franko_vlahov.shows.ShowsAdapter

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SHOW = "EXTRA_SHOW"

        fun buildIntent(activity: Activity, show: Show): Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_SHOW, show)
            return intent
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var show: Show
    private var reviewsAdapter: ReviewsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)


        show = intent.extras?.getSerializable(EXTRA_SHOW) as Show

        setContentView(binding.root)

        initViews()
        initToolbar()
        initReviewsRecycler()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
    }

    private fun initReviewsRecycler() {
        binding.recyclerReviews.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        if (show.reviews != null) {
            reviewsAdapter = ReviewsAdapter(show.reviews!!)
            binding.recyclerReviews.adapter = reviewsAdapter
            binding.labelNoReviews.visibility = View.GONE
        }
        else{
            binding.recyclerReviews.visibility = View.GONE
            binding.ratingShowRating.visibility = View.GONE
            binding.labelAverageRating.visibility = View.GONE
        }
    }

    private fun initViews() {
        binding.labelShowName.text = show.name
        binding.labelShowDescription.text = show.description
        binding.imageShowImage.setImageResource(show.imageResourceId)

        val reviewAverage = calculateAverageRating()

        binding.labelAverageRating.text = String.format(
            resources.getString(R.string.numberOfReviewsAndAverage),
            show.reviews?.size.toString(),
            String.format("%.2f", reviewAverage)
        )
        binding.ratingShowRating.rating = reviewAverage
    }

    private fun calculateAverageRating(): Float {
        var sum = 0f
        show.reviews?.forEach {
            sum += it.rating
        }
        return sum / (show.reviews?.size ?: 1)
    }
}