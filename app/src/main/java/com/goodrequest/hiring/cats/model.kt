package com.goodrequest.hiring.cats

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.goodrequest.hiring.CatsApi
import com.goodrequest.hiring.Result
import com.goodrequest.hiring.map
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class CatsModel(
    state: SavedStateHandle,
    private val context: Context?,
    private val api: CatsApi): ViewModel() {

    val cats = state.getLiveData<Result<List<Cat>>?>("CATS", null)

    fun loadCats() {
        GlobalScope.launch {
            val imagesResult = api.getCatImages(page = 1)

            val catsWithFacts = imagesResult.map { images ->
                images.map(::Cat)
            }

            cats.postValue(catsWithFacts)
        }
    }
}

data class Cat(
    val image: CatImage,
    val fact: CatFact = CatFact("Cat extends Animal"))

@Serializable
data class CatImage(val url: String)

@Serializable
data class CatFact(val text: String)