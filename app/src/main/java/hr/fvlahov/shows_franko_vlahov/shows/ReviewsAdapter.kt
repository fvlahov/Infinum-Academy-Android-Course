package hr.fvlahov.shows_franko_vlahov.shows

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.ViewReviewItemBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.Review

class ReviewsAdapter(
    private var items: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ReviewViewHolder(binding)

        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review){
            binding.labelReview.text = item.comment
            binding.labelReviewerName.text = item.user.email

            try {
                Glide.with(binding.root).load(item.user.imageUrl).circleCrop().into(binding.imageReviewer)
            } catch (e: java.lang.Exception) {
                binding.imageReviewer.setImageResource(R.drawable.ic_profile_placeholder)
            }

            binding.labelRating.text = item.rating.toString()
        }
    }
}