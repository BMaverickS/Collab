package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData

class AddMemberViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getSearchMemberData(userId : String) : LiveData<UserData> = dataSourceRepository.getUserData(userId)
    fun setSaveMember(userData : UserData, groupId : String) = remoteDataSource.setSaveJoinGroup(userData, groupId)
}