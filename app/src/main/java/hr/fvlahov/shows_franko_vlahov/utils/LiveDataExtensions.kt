package hr.fvlahov.shows_franko_vlahov.utils

import androidx.lifecycle.MutableLiveData

//Operator overload, adds to end
operator fun <T> MutableLiveData<List<T>>.plusAssign(item: T) {
    val value = this.value ?: emptyList()
    this.value = value + listOf(item)
}

//Extension for adding item to start of list
fun <T> MutableLiveData<List<T>>.addToStart(item: T) {
    val value = this.value ?: emptyList()
    this.value = listOf(item) + value
}