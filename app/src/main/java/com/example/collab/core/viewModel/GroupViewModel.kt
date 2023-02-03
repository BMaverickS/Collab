package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.ChannelData
import com.example.collab.core.data.MemberData
import com.example.collab.core.data.RemoteDataSource

class GroupViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getChannelListData(groupId : String) : LiveData<List<String>> = dataSourceRepository.getChannelListData(groupId)
    fun getChannelNameData(channelId : ArrayList<String>) : LiveData<List<ChannelData>> = dataSourceRepository.getChannelNameData(channelId)
    fun getMemberListData(groupId : String) : LiveData<List<MemberData>> = dataSourceRepository.getMemberListData(groupId)
    fun setDeleteGroup(groupId : String, userId : String) = remoteDataSource.setDeleteGroup(groupId, userId)
    fun setDeleteChannel(groupId : String) = remoteDataSource.setDeleteChannel(groupId)
}