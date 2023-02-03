package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData

class RegisterViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getRegisterData(email : String) : LiveData<Boolean> = dataSourceRepository.getRegisterData(email)
    fun setRegister(userData : UserData) = remoteDataSource.setRegister(userData)
}