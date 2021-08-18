package hr.fvlahov.shows_franko_vlahov.show_details

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.ShowsApp
import hr.fvlahov.shows_franko_vlahov.databinding.DialogAddReviewBinding
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentShowDetailsBinding
import hr.fvlahov.shows_franko_vlahov.item_decorators.SimpleDividerItemDecoration
import hr.fvlahov.shows_franko_vlahov.model.api_response.Review
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show
import hr.fvlahov.shows_franko_vlahov.preferences.PreferenceHelper
import hr.fvlahov.shows_franko_vlahov.viewmodel.ShowDetailsViewModel
import hr.fvlahov.shows_franko_vlahov.viewmodel.ShowDetailsViewModelFactory

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private val viewModel: ShowDetailsViewModel by viewModels {
        ShowDetailsViewModelFactory((activity?.application as ShowsApp).showsDatabase)
    }


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
            viewLifecycleOwner,
            { show ->
                updateShow(show)
                setRatings(show)
            })
        viewModel.getReviewsLiveData().observe(
            viewLifecycleOwner,
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
            binding.imageShowImage.setImageResource(R.drawable.ic_triangle_white)
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
        binding.recyclerReviews.addItemDecoration(SimpleDividerItemDecoration(requireContext(), R.drawable.line_divider))

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
                    bottomSheetBinding.ratingReviewRating.rating.toInt(),
                    viewModel.getShowLiveData().value?.id?.toInt() ?: 0
                )
                dialog.dismiss()
            } else {
                bottomSheetBinding.labelError.isVisible = true
            }
        }

        dialog.show()
    }

    private fun addReview(comment: String, rating: Int, showId: Int) {
        viewModel.addReview(
            comment = comment,
            rating = rating,
            showId = showId,
            preferenceHelper = PreferenceHelper(activity?.getPreferences(Activity.MODE_PRIVATE))
        )

        updateReviewsAndRatingsVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}