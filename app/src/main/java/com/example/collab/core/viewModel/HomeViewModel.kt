package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.ToDoData
import com.example.collab.core.data.UserData

class HomeViewModel(private val dataSourceRepository : DataSourceRepository) : ViewModel() {

    fun getGroupListData(groupId : ArrayList<String>) : LiveData<List<GroupData>> = dataSourceRepository.getGroupListData(groupId)
    fun getToDoListData(toDoIdList : ArrayList<String>) : LiveData<List<ToDoData>> = dataSourceRepository.getToDoListData(toDoIdList)
    fun getSearchGroupData(groupName : String) : LiveData<Boolean> = dataSourceRepository.getSearchGroupData(groupName)
    fun getUserData(userId : String) : LiveData<UserData> = dataSourceRepository.getUserData(userId)
}