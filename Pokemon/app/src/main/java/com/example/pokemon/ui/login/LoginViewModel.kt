package com.example.pokemon.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.ApiService
import com.example.pokemon.Model.User
import com.example.pokemon.RetrofitClient
import com.example.pokemon.Utils.Common
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class LoginViewModel : ViewModel() {
    private val toastMessageObserver: MutableLiveData<String> = MutableLiveData()

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

    fun getToastObserver(): LiveData<String> {
        return toastMessageObserver
    }


    fun login(email:String,password:String){
        compositeDisposable.add(mservice.login(email,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<User> {
                override fun accept(t: User) {
                    userLiveData!!.value = t
                    Common.currentUser = t
                    //toastMessageObserver!!.setValue("Something unexpected happened to our request: "+t.toString()); // Whenever you want to show toast use setValue.
                }
            }, Consumer { t: Throwable ->
                messageError.value = t.message
                //toastMessageObserver!!.setValue("Something unexpected happened to our request: "+t.message); // Whenever you want to show toast use setValue.
            })
        )
    }
}