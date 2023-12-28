package com.goodrequest.hiring.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.goodrequest.hiring.R
import com.goodrequest.hiring.databinding.ErrorBinding
import com.goodrequest.hiring.databinding.ItemBinding

class PokemonAdapter(private val listener: PokemonAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<PokemonListItem>()

    companion object {
        private const val ITEM_POKEMON = 0
        private const val ITEM_LOADING = 1
        private const val ITEM_ERROR = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_POKEMON -> PokemonItem(
                LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            )

            ITEM_LOADING -> LoadingItem(
                LayoutInflater.from(parent.context).inflate(R.layout.loading, parent, false)
            )

            ITEM_ERROR -> ErrorItem(
                LayoutInflater.from(parent.context).inflate(R.layout.error, parent, false),
                listener
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PokemonItem -> holder.show(items[position] as PokemonListItem.PokemonItem)
            is LoadingItem -> holder.showLoading()
            is ErrorItem -> holder.showError()
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PokemonListItem.PokemonItem -> ITEM_POKEMON
            is PokemonListItem.LoadingItem -> ITEM_LOADING
            is PokemonListItem.ErrorItem -> ITEM_ERROR
        }
    }

    fun show(pokemons: List<PokemonListItem>) {
        items.clear()
        items.addAll(pokemons)
        notifyDataSetChanged()
    }

    class PokemonItem(view: View) : RecyclerView.ViewHolder(view) {
        private val ui = ItemBinding.bind(view)

        fun show(pokemonItem: PokemonListItem.PokemonItem) {
            val pokemon = pokemonItem.pokemon
            ui.image.load(pokemon.detail?.image) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }
            ui.name.text = pokemon.name
            ui.move.text = pokemon.detail?.move
            // -1 is placeholder value if detail loading fails
            if (pokemon.detail != null && pokemon.detail.weight != -1) {
                ui.weight.text = pokemon.detail.weight.toString()
            }
        }
    }

    class LoadingItem(view: View) : RecyclerView.ViewHolder(view) {
        fun showLoading() {
            // Just show loading ProgressBar
        }
    }

    class ErrorItem(view: View, private val listener: PokemonAdapterListener) :
        RecyclerView.ViewHolder(view) {
        private val ui = ErrorBinding.bind(view)

        fun showError() {
            ui.retryButton.visibility = View.VISIBLE
            ui.progressBar.visibility = View.GONE
            ui.retryButton.setOnClickListener {
                ui.retryButton.visibility = View.GONE
                ui.progressBar.visibility = View.VISIBLE
                listener.onRetryButtonClick()
            }
        }
    }
}
