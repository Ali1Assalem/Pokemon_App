package com.example.pokemon.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemon.CallBack.IRecyclerItemClickListener
import com.example.pokemon.Model.Pokemon
import com.example.pokemon.PokemonDetailActivity
import com.example.pokemon.R
import com.example.pokemon.Utils.Common


class PokemonListAdapter(internal var context: Context,
                         internal var pokemonList: List<Pokemon> ) :
    RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
        var tv_pokemon_title: TextView?=null
        var tv_pokemon_power:TextView?=null
        var iv_pokemon_image: ImageView?=null


        init {
            tv_pokemon_title = itemView.findViewById(R.id.tv_pokemon_title) as TextView
            tv_pokemon_power = itemView.findViewById(R.id.tv_pokemon_power) as TextView
            iv_pokemon_image = itemView.findViewById(R.id.iv_pokemon_image) as ImageView
        }


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pokemon,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(Common.IMAGE_URL+pokemonList.get(position).link).into(holder.iv_pokemon_image!!)
        holder.tv_pokemon_title!!.setText(pokemonList.get(position).name)
        holder.tv_pokemon_power!!.setText(pokemonList.get(position).power.toString())

        holder.iv_pokemon_image!!.setOnClickListener(View.OnClickListener {
            Common.pokemonSelected = pokemonList.get(position)
            context.startActivity(Intent(context, PokemonDetailActivity::class.java))
        })

    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }


}
