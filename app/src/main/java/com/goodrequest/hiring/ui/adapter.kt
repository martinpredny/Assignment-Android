package com.goodrequest.hiring.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.goodrequest.hiring.R
import com.goodrequest.hiring.databinding.ItemBinding

class PokemonAdapter: RecyclerView.Adapter<Item>() {
    private val items = ArrayList<Pokemon>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item =
        Item(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: Item, position: Int) =
        holder.show(items[position])

    override fun getItemCount(): Int =
        items.size

    fun show(pokemons: List<Pokemon>) {
        items.clear()
        items.addAll(pokemons)
        notifyDataSetChanged()
    }
}

class Item(view: View): RecyclerView.ViewHolder(view) {
    private val ui = ItemBinding.bind(view)

    fun show(pokemon: Pokemon) {
        ui.image.load(pokemon.detail?.image) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }
        ui.name.text = pokemon.name
        ui.move.text = pokemon.detail?.move
        // -1 is placeholder value if detail loading fails
        if(pokemon.detail != null && pokemon.detail.weight != -1) {
            ui.weight.text = pokemon.detail.weight.toString()
        }
    }
}