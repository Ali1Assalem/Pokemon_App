package com.example.pokemon.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.Model.User
import com.example.pokemon.Utils.Common

class NotificationsViewModel : ViewModel() {

    private val _CurrentUser = MutableLiveData<User>().apply {
        value = Common.currentUser
    }

    val CurrentUser: LiveData<User> = _CurrentUser

}