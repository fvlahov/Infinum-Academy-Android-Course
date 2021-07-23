package hr.fvlahov.shows_franko_vlahov.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.model.Show

class ShowViewModel : ViewModel() {

    private val shows = mutableListOf(
        Show(
            "office",
            "The Office",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_office,
            mutableListOf()
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