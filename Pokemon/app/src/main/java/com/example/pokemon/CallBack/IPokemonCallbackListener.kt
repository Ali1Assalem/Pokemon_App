package com.example.pokemon.CallBack

import com.example.pokemon.Model.Pokemon

interface IPokemonCallbackListener {
    fun onPokemonLoadSuccess(pokemonList:List<Pokemon>)
    fun onPokemonLoadFailed(message:String)
}