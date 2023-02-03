package com.example.collab.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.collab.core.DataSourceRepository
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.MemberData
import com.example.collab.core.data.ToDoData

class ToDoViewModel(private val dataSourceRepository: DataSourceRepository) : ViewModel() {
    fun getToDoListData(toDoIdList : ArrayList<String>) : LiveData<List<ToDoData>> = dataSourceRepository.getToDoListData(toDoIdList)
    fun getGroupListData(groupId : ArrayList<String>) : LiveData<List<GroupData>> = dataSourceRepository.getGroupListData(groupId)
    fun getGroupMemberData(groupId : ArrayList<String>, memberId : String) : LiveData<List<MemberData>> = dataSourceRepository.getGroupMemberData(groupId, memberId)
}