package com.example.testcode.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testcode.R
import com.example.testcode.databinding.ItemStarshipBinding
import com.example.testcode.model.Starship


class StarshipAdapter(private var StarshipList: ArrayList<Starship>) :
    RecyclerView.Adapter<StarshipViewHolder>() {
    private lateinit var onClickListener: OnClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarshipViewHolder {
        val itemStarshipBinding: ItemStarshipBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_starship,
            parent,
            false
        )


        return StarshipViewHolder(itemStarshipBinding)
    }

    override fun getItemCount(): Int = StarshipList.size

    override fun onBindViewHolder(holder: StarshipViewHolder, position: Int) {
        holder.itemStarshipBinding.starship = StarshipList[position]
        holder.itemStarshipBinding.starshipCardView.setOnClickListener {
            onClickListener.onClick(StarshipList[position], position)
        }
        holder.itemStarshipBinding.tvIsMyFav.setOnClickListener {
            onClickListener.onFavriateClick(StarshipList[position], position)
        }
    }

    fun setUpStarships(listOfStarships: List<Starship>) {
        StarshipList.clear()
        StarshipList.addAll(listOfStarships)
        notifyDataSetChanged()
    }

    fun setupClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}

class StarshipViewHolder(val itemStarshipBinding: ItemStarshipBinding) :
    RecyclerView.ViewHolder(itemStarshipBinding.root)

interface OnClickListener {
    fun onClick(item: Starship, position: Int)
    fun onFavriateClick(item: Starship, position: Int)
}