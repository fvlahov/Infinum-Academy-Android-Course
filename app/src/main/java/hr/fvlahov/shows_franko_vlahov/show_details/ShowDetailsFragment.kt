package hr.fvlahov.shows_franko_vlahov.show_details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.DialogAddReviewBinding
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentShowDetailsBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.Review
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show
import hr.fvlahov.shows_franko_vlahov.shows.ReviewsAdapter
import hr.fvlahov.shows_franko_vlahov.viewmodel.ShowDetailsViewModel

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private val viewModel: ShowDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initToolbar()
        initReviewsRecycler()

        viewModel.getShow(args.showId)
        viewModel.getReviewsForShow(args.showId)
        viewModel.getShowLiveData().observe(
            requireActivity(),
            { show ->
                updateShow(show)
                setRatings(show)
            })
        viewModel.getReviewsForShow(args.showId)
        viewModel.getReviewsLiveData().observe(
            requireActivity(),
            { reviews ->
                updateReviews(reviews)
            }
        )
    }

    private fun updateShow(show: Show) {
        binding.labelShowName.text = show.title
        binding.labelShowDescription.text = show.description

        try {
            Glide.with(binding.root).load(show.imageUrl).into(binding.imageShowImage)
        } catch (e: java.lang.Exception) {

        }

        setRatings(show)

        binding.buttonWriteReview.setOnClickListener {
            onWriteReviewClicked()
        }
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateReviews(reviews: List<Review>) {
        reviewsAdapter?.setItems(reviews)
        updateReviewsAndRatingsVisibility()
    }

    private fun initReviewsRecycler() {
        binding.recyclerReviews.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        reviewsAdapter = ReviewsAdapter(listOf())
        binding.recyclerReviews.adapter = reviewsAdapter

        updateReviewsAndRatingsVisibility()
    }

    private fun updateReviewsAndRatingsVisibility() {
        if (reviewsAdapter?.itemCount ?: 0 > 0) {
            binding.labelNoReviews.visibility = View.GONE

            binding.recyclerReviews.visibility = View.VISIBLE
            binding.ratingShowRating.visibility = View.VISIBLE
            binding.labelAverageRating.visibility = View.VISIBLE
        } else {
            binding.labelNoReviews.visibility = View.VISIBLE

            binding.recyclerReviews.visibility = View.GONE
            binding.ratingShowRating.visibility = View.GONE
            binding.labelAverageRating.visibility = View.GONE
        }
    }

    private fun setRatings(show: Show) {

        binding.labelAverageRating.text = String.format(
            resources.getString(R.string.numberOfReviewsAndAverage),
            viewModel.getShowLiveData().value?.numberOfReviews,
            String.format("%.2f", show.averageRating)
        )
        binding.ratingShowRating.rating = show.averageRating ?: 0f
    }

    private fun onWriteReviewClicked() {
        val dialog = BottomSheetDialog(this.requireContext())

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.confirmButton.setOnClickListener {
            if (bottomSheetBinding.ratingReviewRating.rating != 0.0f) {
                addReview(
                    bottomSheetBinding.inputReview.text.toString(),
                    bottomSheetBinding.ratingReviewRating.rating
                )
                dialog.dismiss()
            } else {
                bottomSheetBinding.labelError.visibility = View.VISIBLE
            }
        }

        dialog.show()
    }

    private fun addReview(reviewText: String, rating: Float) {
/*        viewModel.(
            Review(
                "review",
                rating,
                reviewText,
                "testan.testic",
                R.drawable.ic_profile_placeholder
            )
        )*/
        reviewsAdapter?.notifyItemInserted(viewModel.getReviewsLiveData().value?.lastIndex ?: 0)
        updateReviewsAndRatingsVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}