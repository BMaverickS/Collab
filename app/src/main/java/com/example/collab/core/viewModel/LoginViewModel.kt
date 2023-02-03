package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.UserData

class LoginViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    fun getLoginData(email : String) : LiveData<UserData> = dataSourceRepository.getLoginData(email)
}

// LiveData : observable data holder class that is lifecycle aware