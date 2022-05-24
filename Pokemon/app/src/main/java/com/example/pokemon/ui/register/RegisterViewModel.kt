package com.example.pokemon.ui.notifications

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.ApiService
import com.example.pokemon.Model.Pokemon
import com.example.pokemon.Model.User
import com.example.pokemon.RetrofitClient
import com.example.pokemon.Utils.Common
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*

class RegisterViewModel : ViewModel() {

    val retrofitClient= RetrofitClient()
    val mservice = retrofitClient.getClient()!!.create(ApiService::class.java)
    var compositeDisposable = CompositeDisposable()

    private var userLiveData : MutableLiveData<User>?=null
    private var messageError : MutableLiveData<String> = MutableLiveData()


    init {
        getuser()
        getErrorMessage()
    }


    fun getuser() :MutableLiveData<User> {
        if (userLiveData == null) {
            userLiveData = MutableLiveData()
        }
        return userLiveData!!
    }

    fun getErrorMessage():MutableLiveData<String>{
        return messageError
    }

    fun register(name:String,email:String,password:String){
        compositeDisposable.add(mservice.register(name,email,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<User> {
                override fun accept(t: User) {
                    userLiveData!!.value = t
                    Common.currentUser = t
                }
            }, Consumer { t: Throwable ->
                messageError.value = t.message
            })
        )
    }
}