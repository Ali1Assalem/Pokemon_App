package com.example.pokemon

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.Model.Image
import com.example.pokemon.Model.Pokemon
import com.example.pokemon.databinding.ActivityAddPokemonBinding
import com.example.pokemon.databinding.DialogCustomImageSelectionBinding
import com.example.pokemon.ui.home.HomeViewModel
import com.github.drjacky.imagepicker.ImagePicker
import com.inyongtisto.myhelper.extension.toMultipartBody
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddPokemonActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    val mservice = RetrofitClient().getClient()!!.create(ApiService::class.java)
    var compositeDisposable = CompositeDisposable()

    private lateinit var mBinding :ActivityAddPokemonBinding
    var PATH:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddPokemonBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        initViews()
        setUpActionBar()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            mBinding.imgPokemon.setImageURI(uri)
            val fileUri:Uri = uri

            uploadImage(File(fileUri.path!!))
        }
    }

    private fun uploadImage(file: File) {
        val fileImage = file.toMultipartBody()
        mservice.uploadFile(fileImage).enqueue(object :Callback<Image> {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                if (response.body()!!.path!=null)
                    PATH=response.body()!!.path
                Toast.makeText(this@AddPokemonActivity,response.body()!!.success,Toast.LENGTH_LONG).show()

            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Toast.makeText(this@AddPokemonActivity,t.message,Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun initViews() {

        mBinding.imgAddPokemon.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .maxResultSize(512,512)
                .createIntentFromDialog { launcher.launch(it) }
        }

        mBinding.btnAdd.setOnClickListener{
            if(PATH!=null && mBinding.edtName.text !=null && mBinding.edtDescription.text !=null && mBinding.edtType.text !=null && mBinding.edtPower.text !=null) {
                add_pokemon(
                    mBinding.edtName.text.toString(),
                    PATH!!,
                    mBinding.edtDescription.text.toString(),
                    mBinding.edtType.text.toString(),
                    mBinding.edtPower.text.toString()
                )
                PATH = ""
            }else
                Toast.makeText(this@AddPokemonActivity,"Please fill all information..",Toast.LENGTH_LONG).show()
        }


    }

    fun add_pokemon(name:String,link:String,description:String,type:String,power:String){
        compositeDisposable.add(
            mservice.add_pokemon(name, link, description, power.toInt(), type)
                .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Consumer<String> {
                override fun accept(t: String) {
                    Toast.makeText(this@AddPokemonActivity, t, Toast.LENGTH_SHORT).show()
                    homeViewModel.getCategoryList()
                    startActivity(Intent(this@AddPokemonActivity,MainActivity::class.java))
                    finish()
                }
            })
        )
    }

    private fun setUpActionBar(){
        setSupportActionBar(mBinding.toolbarAddPokemon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddPokemon.setNavigationOnClickListener{
            onBackPressed()
        }
    }




}