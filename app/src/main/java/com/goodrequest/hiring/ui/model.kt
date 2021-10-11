package com.goodrequest.hiring.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.goodrequest.hiring.PokemonApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PokemonViewModel(
    state: SavedStateHandle,
    private val context: Context?,
    private val api: PokemonApi) : ViewModel() {

    val pokemons = state.getLiveData<Result<List<Pokemon>>?>("pokemons", null)

    fun load() {
        GlobalScope.launch {
            val result = api.getPokemons(page = 1)
            pokemons.postValue(result)
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