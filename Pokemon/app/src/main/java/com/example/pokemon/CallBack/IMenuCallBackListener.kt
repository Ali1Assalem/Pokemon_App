package com.example.pokemon.CallBack

import com.example.pokemon.Model.Pokemon

interface IMenuCallBackListener {
    fun onMenuLoadSuccess(categoriesList:List<Pokemon>)
    fun onMenuLoadFailed(message:String)
}