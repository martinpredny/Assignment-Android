package com.goodrequest.hiring.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.goodrequest.hiring.PokemonApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PokemonViewModel(
    state: SavedStateHandle,
    private val context: Context?,
    private val api: PokemonApi) : ViewModel() {

    val pokemons = MutableLiveData<Result<List<Pokemon>>?>()
    val refreshError = MutableLiveData<Boolean>()

    fun load() {
        GlobalScope.launch {
            val result = api.getPokemons(page = 1)
            // pokemons were previously loaded successfully
            if(pokemons.value?.getOrDefault(listOf())?.isNotEmpty() == true) {
                if(result.isSuccess) {
                    pokemons.postValue(result)
                } else {
                    refreshError.postValue(true)
                }
            } else {
                pokemons.postValue(result)
            }
        }
    }
}

data class Pokemon(
    val id     : String,
    val name   : String,
    val detail : PokemonDetail? = null)

data class PokemonDetail(
    val image  : String,
    val move   : String,
    val weight : Int)