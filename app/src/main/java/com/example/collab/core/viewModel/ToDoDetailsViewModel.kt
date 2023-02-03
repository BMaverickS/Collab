package com.example.collab.core.viewModel

import androidx.lifecycle.ViewModel
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.ToDoData

class ToDoDetailsViewModel : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun setToDoDetails(toDoData : ToDoData) = remoteDataSource.setToDoDetails(toDoData)
    fun setDeleteToDo(groupId : String, toDoId : String) = remoteDataSource.setDeleteToDo(groupId, toDoId)
    fun setMoveToDoId(toDoId : String, groupIdBefore : String, groupIdAfter : String) = remoteDataSource.setMoveToDoId(toDoId, groupIdBefore, groupIdAfter)
}