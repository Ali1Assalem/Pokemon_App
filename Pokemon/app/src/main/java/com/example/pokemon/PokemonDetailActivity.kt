package com.example.pokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokemon.Utils.Common
import com.example.pokemon.ViewModel.PokemonDetailViewModel
import com.example.pokemon.databinding.ActivityPokemonDetailBinding


class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var pokemonDetailViewModel: PokemonDetailViewModel
    private lateinit var binding:ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemonDetailViewModel = ViewModelProvider(this).get(PokemonDetailViewModel::class.java)

        pokemonDetailViewModel.getMutableLivePokemon().observe(this, Observer {
            Glide.with(this).load(Common.IMAGE_URL+it.link)
                .into(binding.imgPokemon)
            binding.pokemonName.text = it.name
            binding.pokemonPower.text = it.power.toString()
            binding.pokemonType.text = it.type
            binding.pokemonDescription.text = it.description
        })

        setUpActionBar()
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarAddPokemon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddPokemon.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit ->{
                startActivity(Intent(this,UpdatePokemonActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}