package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData

class JoinGroupViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getJoinGroupData() : LiveData<GroupData> = dataSourceRepository.getJoinGroupData()
    fun setSaveJoinGroup(userData : UserData, groupId : String) = remoteDataSource.setSaveJoinGroup(userData, groupId)
}