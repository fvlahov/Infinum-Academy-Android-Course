package hr.fvlahov.shows_franko_vlahov.shows

import android.content.ClipDescription
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.ViewShowItemBinding
import hr.fvlahov.shows_franko_vlahov.model.api_response.Show
import java.lang.Exception

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: ViewShowItemBinding = ViewShowItemBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {

        setViewPaddings(context)
        clipToPadding = false
    }

    private fun setViewPaddings(context: Context) {
        val pixelTopLeftRightPaddings =
            context.resources.getDimensionPixelSize(R.dimen.show_card_top_left_right_padding)
        val pixelBottomPadding =
            context.resources.getDimensionPixelSize(R.dimen.show_card_bottom_padding)

        setPadding(
            pixelTopLeftRightPaddings,
            pixelTopLeftRightPaddings,
            pixelTopLeftRightPaddings,
            pixelBottomPadding
        )
    }

    fun setTitle(title: String) {
        binding.labelShowName.text = title
    }

    fun setDescription(description: String?) {
        binding.labelShowDescription.text = description
    }

    fun setImage(imageUrl: String?){
        try {
            Glide.with(binding.root).load(imageUrl)
                .into(binding.imageShowImage)
        } catch (e: Exception) {
            Log.d("Glide", e.message.toString())
        }
    }

    fun setCardOnClickListener(listener: OnClickListener){
        binding.cardShows.setOnClickListener(listener)
    }

    fun setShow(show: Show) {
        setTitle(show.title)
        setDescription(show.description)
        setImage(show.imageUrl)
    }
}