package com.example.pokemon.ui.home

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.ApiService
import com.example.pokemon.CallBack.IMenuCallBackListener
import com.example.pokemon.Model.Pokemon
import com.example.pokemon.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*


class HomeViewModel : ViewModel(), IMenuCallBackListener {

    val retrofitClient=RetrofitClient()
    val mservice = retrofitClient.getClient()!!.create(ApiService::class.java)
    var compositeDisposable = CompositeDisposable()


    override fun onMenuLoadSuccess(categoriesList: List<Pokemon>) {
        categoriesListMutable!!.value = categoriesList
    }

    override fun onMenuLoadFailed(message: String) {
        messageError.value = message
    }

    private var categoriesListMutable : MutableLiveData<List<Pokemon>>?=null
    private var messageError : MutableLiveData<String> = MutableLiveData()
    private val categoryCallBackListener:IMenuCallBackListener


    init {
        categoryCallBackListener = this
        getCategoryList()
        getErrorMessage()
    }
    fun getCategoryList() :MutableLiveData<List<Pokemon>> {
        if (categoriesListMutable == null) {
            categoriesListMutable = MutableLiveData()
            loadMenu()
        }
        return categoriesListMutable!!
    }

    fun getErrorMessage():MutableLiveData<String>{
        return messageError
    }

    fun loadMenu(){
        compositeDisposable.add(mservice.getPokemon()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Pokemon>> {
                override fun accept(t: List<Pokemon>) {
                    Collections.reverse(t);
                    categoryCallBackListener.onMenuLoadSuccess(t)
                }
            }, Consumer { t: Throwable ->
                categoryCallBackListener.onMenuLoadFailed(t.message!!)
            })
        )
    }





//    fun loadCategory(){
//        val tempList = ArrayList<CategoryModel>()
//
//        val CategoryRef = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF)
//        CategoryRef.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (itemSnapShot in snapshot!!.children)
//                {
//                    val model= itemSnapShot.getValue<CategoryModel>(CategoryModel::class.java)
//                    model!!.menu_id = itemSnapShot.key
//                    tempList.add(model!!)
//                }
//                categoryCallBackListener.onCategoryLoadSuccess(tempList)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                categoryCallBackListener.onCategoryLoadFailed(error.message)
//            }
//
//        })
//
//
//    }


}