package com.example.pokemon

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokemon.Model.Image
import com.example.pokemon.Utils.Common
import com.example.pokemon.ViewModel.PokemonDetailViewModel
import com.example.pokemon.databinding.ActivityPokemonDetailBinding
import com.example.pokemon.databinding.ActivityUpdatePokemonBinding
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

class UpdatePokemonActivity : AppCompatActivity() {
    val mservice = RetrofitClient().getClient()!!.create(ApiService::class.java)
    var compositeDisposable = CompositeDisposable()
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var pokemonDetailViewModel: PokemonDetailViewModel
    private lateinit var binding: ActivityUpdatePokemonBinding
    lateinit var path:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        pokemonDetailViewModel = ViewModelProvider(this).get(PokemonDetailViewModel::class.java)

//        pokemonDetailViewModel.getMutableLivePokemon().observe(this, Observer {
//            Glide.with(this).load(Common.IMAGE_URL+it.link)
//                .into(binding.imagePokemon)
//            binding.name.setText(it.name)
//            binding.power.setText(it.power.toString())
//            binding.type.setText(it.type)
//            binding.desc.setText(it.description)
//            path = it.link.toString()
//        })

        initViews()
        setUpActionBar()

        binding.btnDelete.setOnClickListener{
            delete_pokemon(pokemonDetailViewModel.getMutableLivePokemon().value!!.id)
        }

        binding.buttonUpdate.setOnClickListener{
                if( binding.name.text !=null && binding.desc.text !=null && binding.type.text !=null && binding.power.text !=null) {
                    update_pokemon(
                        pokemonDetailViewModel.getMutableLivePokemon().value!!.id,
                        binding.name.getText().toString(),
                        path,
                        binding.desc.getText().toString(),
                        binding.power.getText().toString(),
                        binding.type.getText().toString(),
                    )
                    path = ""
                }else
                    Toast.makeText(this@UpdatePokemonActivity,"Please fill all information..",Toast.LENGTH_LONG).show()
            }
        }



    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarAddPokemon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddPokemon.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun delete_pokemon(id:String){
        compositeDisposable.add(
            mservice.delete_pokemon(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Consumer<String> {
                    override fun accept(t: String) {
                        Toast.makeText(this@UpdatePokemonActivity, t, Toast.LENGTH_SHORT).show()
                        homeViewModel.getCategoryList()
                        startActivity(Intent(this@UpdatePokemonActivity,MainActivity::class.java))
                        finish()
                    }
                })
        )
    }

    fun update_pokemon(id: String,name:String,link:String,description:String,power:String,type:String){
        compositeDisposable.add(
            mservice.update_pokemon(id,name, link, description, power, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Consumer<String> {
                    override fun accept(t: String) {
                        Toast.makeText(this@UpdatePokemonActivity, t, Toast.LENGTH_SHORT).show()
                        homeViewModel.getCategoryList()
                        startActivity(Intent(this@UpdatePokemonActivity,MainActivity::class.java))
                        finishAffinity()
                    }
                })
        )
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            binding.imagePokemon.setImageURI(uri)
            val fileUri: Uri = uri

            uploadImage(File(fileUri.path!!))
        }
    }

    private fun uploadImage(file: File) {
        val fileImage = file.toMultipartBody()
        mservice.uploadFile(fileImage).enqueue(object : Callback<Image> {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                if (response.body()!!.path!=null)
                    path=response.body()!!.path
                Toast.makeText(this@UpdatePokemonActivity,response.body()!!.success,Toast.LENGTH_LONG).show()

            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Toast.makeText(this@UpdatePokemonActivity,t.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initViews() {
        binding.imgAddPokemon.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .maxResultSize(512,512)
                .createIntentFromDialog { launcher.launch(it) }
        }

        Glide.with(this).load(Common.IMAGE_URL+Common.pokemonSelected!!.link)
            .into(binding.imagePokemon)
        binding.name.setText(Common.pokemonSelected!!.name)
        binding.power.setText(Common.pokemonSelected!!.power.toString())
        binding.type.setText(Common.pokemonSelected!!.type)
        binding.desc.setText(Common.pokemonSelected!!.description)
        path = Common.pokemonSelected!!.link
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}