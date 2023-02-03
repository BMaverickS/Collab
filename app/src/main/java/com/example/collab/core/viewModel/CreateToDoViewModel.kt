package com.example.collab.core.viewModel

import androidx.lifecycle.ViewModel
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.ToDoData

class CreateToDoViewModel : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun setCreateToDo(toDoData : ToDoData, saveGroupId : String) = remoteDataSource.setCreateToDo(toDoData, saveGroupId)
    fun getGroupMemberData(groupId : ArrayList<String>, memberId : String) = remoteDataSource.getGroupMemberData(groupId, memberId)
}