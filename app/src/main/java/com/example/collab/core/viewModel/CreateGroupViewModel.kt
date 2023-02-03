package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.RemoteDataSource

class CreateGroupViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getGroupData(groupName : String, groupId : String) : LiveData<List<Boolean>> = dataSourceRepository.getGroupData(groupName, groupId)
    fun setCreateGroup(groupData : GroupData) = remoteDataSource.setCreateGroup(groupData)
}