package com.example.pokemon.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.Model.Pokemon
import com.example.pokemon.Utils.Common

class PokemonDetailViewModel : ViewModel() {
    private var mutableLivePokemon: MutableLiveData<Pokemon>?=null

    init {
        getMutableLivePokemon()
    }

    fun getMutableLivePokemon():MutableLiveData<Pokemon>{
        if (mutableLivePokemon == null)
            mutableLivePokemon = MutableLiveData()
        mutableLivePokemon!!.value = Common.pokemonSelected
        return mutableLivePokemon!!
    }

    fun setPokemonModel(pokemon: Pokemon) {
        if (mutableLivePokemon != null)
            mutableLivePokemon!!.value = pokemon
    }

}