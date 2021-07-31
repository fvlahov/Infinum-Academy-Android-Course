package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.model.Review
import hr.fvlahov.shows_franko_vlahov.model.Show

class ShowViewModel : ViewModel() {

    private val officeReviews = mutableListOf(
        Review(
            "review1",
            3.7f,
            "This show was a complete masterpiece, I really liked it.",
            "imenko.prezimenovic",
            R.drawable.ic_profile_placeholder
        ),
        Review("review2", 3.5f, "", "branimir.akmadzic", R.drawable.ic_profile_placeholder),
        Review(
            "review3",
            3.7f,
            "It was good. I laughed a lot, it matches my sense of humor perfectly. Loved it!",
            "testamenko.testovic",
            R.drawable.ic_profile_placeholder
        ),
    )

    private val shows = mutableListOf(
        Show(
            "office",
            "The Office",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_office,
            officeReviews
        ),
        Show(
            "strangerThings",
            "Stranger Things",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_stranger_things,
            mutableListOf()
        ),
        Show(
            "bloodAintWater",
            "Krv nije Voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_krv_nije_voda,
            mutableListOf()
        )
    )

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }

    fun addShow(show: Show) {
        shows.add(show)
        showsLiveData.value = shows
    }
}