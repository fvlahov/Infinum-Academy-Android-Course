package hr.fvlahov.shows_franko_vlahov.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.fvlahov.shows_franko_vlahov.databinding.ViewShowItemBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    /**
     * Called when RecyclerView needs a new ViewHolder to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(ShowCardView(parent.context))
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
        holder.getShowsCardView().setShow(items[position])
        holder.getShowsCardView().setCardOnClickListener {
            onClickCallback(items[position])
        }
    }

    fun setItems(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    /**
     * Custom-made ViewHolder, used to match the data to the concrete view.
     */
    inner class ShowViewHolder(private val showCardView: ShowCardView) :
        RecyclerView.ViewHolder(showCardView) {

        fun getShowsCardView(): ShowCardView {
            return showCardView
        }
    }
}