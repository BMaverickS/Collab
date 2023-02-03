package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.ChatData
import com.example.collab.core.data.RemoteDataSource

class ChatViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun getChatData(channelId : String) : LiveData<List<ChatData>> = dataSourceRepository.getChatData(channelId)
    fun setChatMessage(channelId : String, chatData : ChatData) = remoteDataSource.setChatMessage(channelId, chatData)
}