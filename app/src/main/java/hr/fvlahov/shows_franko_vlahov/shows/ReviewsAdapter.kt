package hr.fvlahov.shows_franko_vlahov.shows

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.fvlahov.shows_franko_vlahov.databinding.ItemReviewBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.Review

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

    fun setItems(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review){
            binding.labelReview.text = item.comment
            binding.labelReviewerName.text = item.user.email

            try {
                binding.imageReviewer.setImageURI(Uri.parse(item.user.imageUrl))
            }
            catch(e: Exception){

            }

            binding.labelRating.text = item.rating.toString()
        }
    }
}