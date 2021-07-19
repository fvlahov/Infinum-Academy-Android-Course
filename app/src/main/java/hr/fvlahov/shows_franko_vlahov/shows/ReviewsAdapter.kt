package hr.fvlahov.shows_franko_vlahov.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.fvlahov.shows_franko_vlahov.databinding.ItemReviewBinding
import hr.fvlahov.shows_franko_vlahov.databinding.ViewShowItemBinding
import hr.fvlahov.shows_franko_vlahov.model.Review

class ReviewsAdapter(
    private var items: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ReviewViewHolder(binding)

        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review){
            binding.labelReview.text = item.review
            binding.labelReview.visibility = if(item.review.isEmpty()) View.GONE else View.VISIBLE
            binding.labelReviewerName.text = item.reviewerName
            binding.imageReviewer.setImageResource(item.reviewerImageResourceId)
            binding.labelRating.text = item.rating.toString()
        }
    }
}