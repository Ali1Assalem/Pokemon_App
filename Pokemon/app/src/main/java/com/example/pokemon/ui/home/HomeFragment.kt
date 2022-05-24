package com.example.pokemon.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.Adapter.PokemonListAdapter
import com.example.pokemon.AddPokemonActivity
import com.example.pokemon.R
import com.example.pokemon.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var recycler_pokemon:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.getCategoryList().observe( viewLifecycleOwner , Observer{
            val listData = it
            val adapter =PokemonListAdapter(requireContext(),listData)
            recycler_pokemon!!.adapter=adapter
        })

        homeViewModel.getErrorMessage().observe( viewLifecycleOwner , Observer{
            Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_LONG).show()
        })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView(root)
        return root
    }

    private fun initView(view: View) {
        recycler_pokemon =view.findViewById(R.id.rv_pokemon_list) as RecyclerView
        recycler_pokemon!!.setHasFixedSize(true)
        recycler_pokemon!!.layoutManager = GridLayoutManager(context,2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add ->{
                startActivity(Intent(requireContext(),AddPokemonActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}