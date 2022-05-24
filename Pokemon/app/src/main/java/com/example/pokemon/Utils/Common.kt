package com.example.pokemon.Utils

import com.example.pokemon.Model.Pokemon
import com.example.pokemon.Model.User


object Common {
    val BASE_URL =  "http://192.168.137.1:8000/"
    val IMAGE_URL =  BASE_URL+"storage/pokemon/"
    val IMAGE_URL_PROFILE =  BASE_URL+"storage/profile/"

    var pokemonSelected : Pokemon?=null
    var currentUser : User?=null

}