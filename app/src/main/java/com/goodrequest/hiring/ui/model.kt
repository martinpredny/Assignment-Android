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
            if(result.isSuccess) {
                    val pokemonList = result.getOrDefault(listOf())
                    fetchDetailsAndUpdateLiveData(pokemonList)
            } else {
                if (pokemons.value?.getOrDefault(listOf())?.isNotEmpty() == true) {
                    // Pokemons were previously loaded successfully, just show snackbar
                    refreshError.postValue(true)
                } else {
                    // Pokemons were not loaded previously, show retry button
                    pokemons.postValue(result)
                }
            }
        }
    }

    private suspend fun fetchDetailsAndUpdateLiveData(pokemonList: List<Pokemon>) {
        // Fetch details for each Pokemon
        val detailedPokemonList = pokemonList.map { pokemon ->
            val detailResult = api.getPokemonDetail(pokemon)
            val detail = if (detailResult.isSuccess) {
                detailResult.getOrDefault(PokemonDetail("", "", -1))
            } else {
                // Handle fetching detail failure
                PokemonDetail("", "", -1)
            }
            pokemon.copy(detail = detail)
        }
        pokemons.postValue(Result.success(detailedPokemonList))
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