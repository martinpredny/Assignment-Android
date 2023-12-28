package com.goodrequest.hiring.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.goodrequest.hiring.PokemonApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PokemonViewModel(
    state: SavedStateHandle,
    private val api: PokemonApi
) : ViewModel() {

    val pokemons = MutableLiveData<Result<List<PokemonListItem>>>()
    val refreshErrorOccurred = MutableLiveData<Boolean>()
    private var currentPage = 1
    private var isLoadingNextPage = false

    fun addLoadingItemToRecycler() {
        val loaderItem = PokemonListItem.LoadingItem
        val currentList = pokemons.value?.getOrDefault(emptyList()) ?: emptyList()
        val updatedList = currentList + loaderItem
        pokemons.postValue(Result.success(updatedList))
    }

    private fun addErrorItemToRecycler() {
        val errorItem = PokemonListItem.ErrorItem
        val currentList = pokemons.value?.getOrDefault(emptyList())
            // We need only one of Error / Loading items at bottom, so filter them out
            ?.filter { it !is PokemonListItem.LoadingItem && it !is PokemonListItem.ErrorItem }
            ?: emptyList()
        val updatedList = currentList + errorItem
        pokemons.postValue(Result.success(updatedList))
    }

    fun loadFirstPage() {
        currentPage = 1
        GlobalScope.launch {
            val result = api.getPokemons(currentPage)
            if (result.isSuccess) {
                val pokemonList = result.getOrDefault(emptyList()).map { PokemonListItem.PokemonItem(it) }
                fetchDetailsAndUpdateLiveData(pokemonList, isNextPage = false)
            } else {
                if (pokemons.value?.getOrDefault(emptyList())?.isNotEmpty() == true) {
                    // Pokemons were previously loaded successfully, just show snack bar
                    refreshErrorOccurred.postValue(true)
                } else {
                    // Pokemons were not loaded previously, show retry button
                    val errorResult = Result.failure<List<PokemonListItem>>(result.exceptionOrNull() ?: Exception("Unknown Error Occurred!"))
                    pokemons.postValue(errorResult)
                }
            }
            isLoadingNextPage = false
        }
    }

    fun loadNextPage() {
        GlobalScope.launch {
            val result = api.getPokemons(page = currentPage + 1)
            if (result.isSuccess) {
                val pokemonList = result.getOrDefault(emptyList()).map { PokemonListItem.PokemonItem(it) }
                fetchDetailsAndUpdateLiveData(pokemonList, isNextPage = true)
                currentPage++
            } else {
                addErrorItemToRecycler()
            }
        }
    }

    private suspend fun fetchDetailsAndUpdateLiveData(
        pokemonList: List<PokemonListItem>,
        isNextPage: Boolean
    ) {
        // Fetch details for each Pokemon
        val detailedPokemonList =
            pokemonList.filterIsInstance<PokemonListItem.PokemonItem>().map { pokemonItem ->
                val pokemon = pokemonItem.pokemon
                val detailResult = api.getPokemonDetail(pokemon)
                val detail = if (detailResult.isSuccess) {
                    detailResult.getOrDefault(PokemonDetail("", "", -1))
                } else {
                    // Handle fetching detail failure
                    PokemonDetail("", "", -1)
                }
                PokemonListItem.PokemonItem(pokemon.copy(detail = detail))
            }

        if (isNextPage) {
            // Not first page, so append new items at the end of the list for pagination
            val updatedList = if (pokemons.value != null) {
                pokemons.value?.getOrThrow()
                    ?.filter { it !is PokemonListItem.LoadingItem && it !is PokemonListItem.ErrorItem }
                    ?.plus(detailedPokemonList)
                    ?: detailedPokemonList
            } else {
                detailedPokemonList
            }
            pokemons.postValue(Result.success(updatedList))
            isLoadingNextPage = false
        } else {
            // Update LiveData with pokemon list for the first page
            pokemons.postValue(Result.success(detailedPokemonList))
        }
    }

    fun isLoadingNextPage(): Boolean = isLoadingNextPage

    fun setLoadingNextPage(loading: Boolean) {
        isLoadingNextPage = loading
    }
}

sealed class PokemonListItem {
    data class PokemonItem(val pokemon: Pokemon) : PokemonListItem()
    object LoadingItem : PokemonListItem()
    object ErrorItem : PokemonListItem()
}

data class Pokemon(
    val id     : String,
    val name   : String,
    val detail : PokemonDetail? = null)

data class PokemonDetail(
    val image  : String,
    val move   : String,
    val weight : Int)