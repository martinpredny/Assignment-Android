package com.goodrequest.hiring.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.goodrequest.hiring.R
import com.goodrequest.hiring.databinding.CatItemBinding

class CatsAdapter: RecyclerView.Adapter<CatItem>() {
    private val items = ArrayList<Cat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatItem =
        CatItem(LayoutInflater.from(parent.context).inflate(R.layout.cat_item, parent, false))

    override fun onBindViewHolder(holder: CatItem, position: Int) =
        holder.show(items[position])

    override fun getItemCount(): Int =
        items.size

    fun show(cats: List<Cat>) {
        items.clear()
        items.addAll(cats)
        notifyDataSetChanged()
    }
}

class CatItem(view: View): RecyclerView.ViewHolder(view) {
    private val ui = CatItemBinding.bind(view)

    fun show(cat: Cat) {
        ui.image.load(cat.image.url) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        ui.title.text = cat.fact.text
    }
}