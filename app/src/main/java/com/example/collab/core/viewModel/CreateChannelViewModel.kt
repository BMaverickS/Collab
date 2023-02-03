package com.example.collab.core.viewModel

import androidx.lifecycle.ViewModel
import com.example.collab.core.data.RemoteDataSource

class CreateChannelViewModel : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun setChannel(groupId : String, channelName : String) = remoteDataSource.setChannel(groupId, channelName)
}