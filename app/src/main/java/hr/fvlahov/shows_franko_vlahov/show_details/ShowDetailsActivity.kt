package hr.fvlahov.shows_franko_vlahov.show_details

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.ActivityShowDetailsBinding
import hr.fvlahov.shows_franko_vlahov.databinding.DialogAddReviewBinding
import hr.fvlahov.shows_franko_vlahov.model.Review
import hr.fvlahov.shows_franko_vlahov.model.Show
import hr.fvlahov.shows_franko_vlahov.shows.ReviewsAdapter

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

        setContentView(binding.root)

        show = intent.extras?.getSerializable(EXTRA_SHOW) as Show

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
        reviewsAdapter = ReviewsAdapter(show.reviews)
        binding.recyclerReviews.adapter = reviewsAdapter

        //Item decorator
/*        val divider = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )

        divider.setDrawable(ShapeDrawable().apply {
            intrinsicHeight = resources.getDimensionPixelOffset(R.dimen.recyclerDividerWeight)
            paint.color = Color.GRAY
        })
        binding.recyclerReviews.addItemDecoration(divider)*/

        updateReviewsAndRatingsVisibility()
    }

    private fun updateReviewsAndRatingsVisibility() {
        if (reviewsAdapter?.itemCount ?: 0 > 0) {
            binding.labelNoReviews.visibility = View.GONE

            binding.recyclerReviews.visibility = View.VISIBLE
            binding.ratingShowRating.visibility = View.VISIBLE
            binding.labelAverageRating.visibility = View.VISIBLE
            setRatings()
        } else {
            binding.labelNoReviews.visibility = View.VISIBLE

            binding.recyclerReviews.visibility = View.GONE
            binding.ratingShowRating.visibility = View.GONE
            binding.labelAverageRating.visibility = View.GONE
        }
    }

    private fun initViews() {
        binding.labelShowName.text = show.name
        binding.labelShowDescription.text = show.description
        binding.imageShowImage.setImageResource(show.imageResourceId)

        setRatings()

        binding.buttonWriteReview.setOnClickListener {
            onWriteReviewClicked()
        }
    }

    private fun setRatings() {
        val reviewAverage = calculateAverageRating()

        binding.labelAverageRating.text = String.format(
            resources.getString(R.string.numberOfReviewsAndAverage),
            show.reviews.size.toString(),
            String.format("%.2f", reviewAverage)
        )
        binding.ratingShowRating.rating = reviewAverage
    }

    private fun onWriteReviewClicked() {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.confirmButton.setOnClickListener {
            if (bottomSheetBinding.ratingReviewRating.rating != 0.0f) {
                addReview(
                    bottomSheetBinding.inputReview.text.toString(),
                    bottomSheetBinding.ratingReviewRating.rating.toInt()
                )
                dialog.dismiss()
            } else {
                bottomSheetBinding.labelError.visibility = View.VISIBLE
            }

        }

        dialog.show()
    }

    private fun addReview(reviewText: String, rating: Int) {
        show.reviews.add(
            Review(
                "review",
                rating,
                reviewText,
                "testan.testic",
                R.drawable.ic_profile_placeholder
            )
        )
        reviewsAdapter?.notifyItemInserted(show.reviews.lastIndex)
        updateReviewsAndRatingsVisibility()
    }

    private fun calculateAverageRating(): Float {
        val averageRating = show.reviews.map { review -> review.rating }.average()

        return averageRating.toFloat()
    }
}