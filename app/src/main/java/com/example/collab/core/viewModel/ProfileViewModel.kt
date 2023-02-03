package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData

class ProfileViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getUserData(userId : String) : LiveData<UserData> = dataSourceRepository.getSingleUserData(userId)
    fun getProfileData(email : String) : LiveData<Boolean> = dataSourceRepository.getRegisterData(email)
    fun setProfile(userData : UserData) = remoteDataSource.setProfile(userData)
}