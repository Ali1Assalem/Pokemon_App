package com.example.pokemon.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.Utils.Common
import com.example.pokemon.databinding.FragmentRegisterBinding
import com.example.pokemon.ui.notifications.LoginViewModel
import com.example.pokemon.ui.notifications.RegisterViewModel

class RegisterFragment : Fragment() {

private var _binding: FragmentRegisterBinding? = null

// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    val registerViewModel =
        ViewModelProvider(this).get(RegisterViewModel::class.java)


    _binding = FragmentRegisterBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.btnRegister.setOnClickListener{
        registerViewModel.register(binding.edtName.getText().toString(),binding.edtEmail.getText().toString(),binding.edtPassword.getText().toString())
    }

    registerViewModel.getuser().observe( viewLifecycleOwner , Observer{
        if (it != null)
            findNavController().navigate(R.id.action_navigation_register_to_navigation_profile)
        Common.currentUser = it
    })
    registerViewModel.getErrorMessage().observe( viewLifecycleOwner , Observer{
        Toast.makeText(requireContext(),it.toString(), Toast.LENGTH_LONG).show()
    })

    binding.txtSignIn.setOnClickListener{
        findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
    }


    return root
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}