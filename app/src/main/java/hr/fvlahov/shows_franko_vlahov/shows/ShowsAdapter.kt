package hr.fvlahov.shows_franko_vlahov.shows

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.fvlahov.shows_franko_vlahov.databinding.ViewShowItemBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show
import java.lang.Exception

class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    /**
     * Called when RecyclerView needs a new ViewHolder to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        val holder = ShowViewHolder(binding)

        return holder
    }

    /**
     * Returns the total number of items that can be shown.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    fun addItem(show: Show) {
        items = items + show
        notifyItemInserted(items.lastIndex)
    }

    /**
     * Custom-made ViewHolder, used to match the data to the concrete view.
     */
    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.labelShowName.text = item.title
            binding.labelShowDescription.text = item.description
            try {
                binding.imageShowImage.setImageURI(Uri.parse(item.imageUrl))
            } catch (e: Exception) {

            }
            binding.cardShows.setOnClickListener {
                onClickCallback(item)
            }
        }
    }


}