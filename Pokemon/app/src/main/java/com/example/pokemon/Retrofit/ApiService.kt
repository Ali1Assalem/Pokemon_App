package com.example.pokemon

import com.example.pokemon.Model.Image
import com.example.pokemon.Model.Pokemon
import com.example.pokemon.Model.User
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("api/pokemon")
    fun getPokemon(): Observable<List<Pokemon>>

    @FormUrlEncoded
    @POST("api/add_pokemon")
    fun add_pokemon(
        @Field("name") name: String?,
        @Field("link") link: String?,
        @Field("description") description: String?,
        @Field("power") power: Int?,
        @Field("type") type: String?,
        ): Observable<String>

    @Multipart
    @POST("api/uploadPokemonImage")
    fun uploadFile( @Part image: MultipartBody.Part?): Call<Image>

    @FormUrlEncoded
    @POST("api/delete_pokemon")
    fun delete_pokemon(
        @Field("id") id: String?
    ): Observable<String>

    @FormUrlEncoded
    @POST("api/update_pokemon")
    fun update_pokemon(
        @Field("id") id: String?,
        @Field("name") name: String?,
        @Field("link") link: String?,
        @Field("description") description: String?,
        @Field("power") power: String?,
        @Field("type") type: String?,
    ): Observable<String>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String): Observable<User>

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String): Observable<User>

    @Multipart
    @POST("api/updateProfile")
    fun uploadFile(@Part email: MultipartBody.Part, @Part image: MultipartBody.Part): Call<Image>

    @FormUrlEncoded
    @POST("api/update_profile")
    fun update_profile(
        @Field("id") id: Int,
        @Field("image") image: String
    ): Call<String>

/*
    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String,
            @Field("fcm") fcm: String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("fcm") fcm: String
    ): Call<ResponModel>


    @GET("produk")
    fun getProduk(): Call<ResponModel>

    @GET("province")
    fun getProvinsi(
        @Header("key") key: String
    ): Call<ResponModel>

    @GET("city")
    fun getKota(
        @Header("key") key: String,
        @Query("province") id: String
    ): Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id: Int
    ): Call<ResponModel>


    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
        @Header("key") key: String,
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("weight") weight: Int,
        @Field("courier") courier: String
    ): Call<ResponOngkir>

    @POST("chekout")
    fun chekout(
        @Body data: Chekout
    ): Call<ResponModel>

    @GET("chekout/user/{id}")
    fun getRiwayat(
        @Path("id") id: Int
    ): Call<ResponModel>


    @POST("chekout/batal/{id}")
    fun batalChekout(
        @Path("id") id: Int
    ): Call<ResponModel>
    */
}