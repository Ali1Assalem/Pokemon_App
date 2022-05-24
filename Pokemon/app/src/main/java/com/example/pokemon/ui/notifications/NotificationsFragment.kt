package com.example.pokemon.ui.notifications

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.pokemon.ApiService
import com.example.pokemon.Model.Image
import com.example.pokemon.R
import com.example.pokemon.RetrofitClient
import com.example.pokemon.Utils.Common
import com.example.pokemon.databinding.FragmentNotificationsBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.inyongtisto.myhelper.extension.toMultipartBody
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class NotificationsFragment : Fragment() {
    val mservice = RetrofitClient().getClient()!!.create(ApiService::class.java)
    private var _binding: FragmentNotificationsBinding? = null
    var PATH:String?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

        //findNavController().navigate(R.id.action_navigation_profile_to_navigation_register)

//        notificationsViewModel.CurrentUser.observe(viewLifecycleOwner) {
//            if(it!=null){
//                binding.name.setText(it.name)
//                binding.email.text = it.email
//                Glide.with(this).load(Common.IMAGE_URL_PROFILE + it.image)
//                        .into(binding.imgProfile)
//
//            }else {
//
//            }
//        }
        if(Common.currentUser!=null){
            binding.name.setText(Common.currentUser!!.name)
            binding.email.setText (Common.currentUser!!.email)
            //Glide.with(this).load(Common.IMAGE_URL_PROFILE + it.image)
            //    .into(binding.imgProfile)
        }

        binding.imgProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_register)
        }

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        initViews()
        return root
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            _binding!!.imgProfile.setImageURI(uri)
            val fileUri: Uri = uri

            uploadImage(File(fileUri.path!!))
        }
    }

    private fun uploadImage(file: File) {
        val fileImage = file.toMultipartBody()
        val userEmail: MultipartBody.Part =
            MultipartBody.Part.createFormData("email", Common.currentUser!!.email)

        mservice.uploadFile(userEmail,fileImage!!).enqueue(object : Callback<Image> {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                if (response.body()!!.path!=null)
                    Common.currentUser!!.image=response.body()!!.path
                Toast.makeText(requireContext(),response.body()!!.success, Toast.LENGTH_LONG).show()

            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Toast.makeText(requireContext(),t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initViews() {

        _binding!!.addImg.setOnClickListener {
            ImagePicker.with(requireActivity())
                .crop()
                .maxResultSize(512, 512)
                .createIntentFromDialog { launcher.launch(it) }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_logout,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout ->{
                //startActivity(Intent(requireContext(), AddPokemonActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}