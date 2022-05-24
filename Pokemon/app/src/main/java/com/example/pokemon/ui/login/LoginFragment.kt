package com.example.pokemon.ui.login

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
import com.example.pokemon.databinding.FragmentLoginBinding
import com.example.pokemon.ui.notifications.LoginViewModel

class LoginFragment : Fragment() {

private var _binding: FragmentLoginBinding? = null

// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    val loginViewModel =
        ViewModelProvider(this).get(LoginViewModel::class.java)


    _binding = FragmentLoginBinding.inflate(inflater, container, false)
    val root: View = binding.root

    binding.btnLogin.setOnClickListener{
        loginViewModel.login(binding.edtEmail.getText().toString(),binding.edtPassword.getText().toString())
    }

//    loginViewModel.getToastObserver().observe(requireActivity()) { message ->
//        Toast.makeText(
//            requireContext(),
//            message,
//            Toast.LENGTH_SHORT
//        ).show()
//    }

    loginViewModel.getuser().observe( viewLifecycleOwner , Observer{
        if (it.name != null)
            findNavController().navigate(R.id.action_navigation_login_to_navigation_profile)
        Common.currentUser = it
    })
    loginViewModel.getErrorMessage().observe( viewLifecycleOwner , Observer{
        Toast.makeText(requireContext(),it.toString(), Toast.LENGTH_LONG).show()
    })

    binding.txtSignUP.setOnClickListener{
        findNavController().navigate(R.id.action_navigation_login_to_navigation_register)
    }


    return root
}


override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}