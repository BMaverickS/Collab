package com.example.collab.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoData (
    val toDoId : String,
    val toDoTitle : String,
    val beginDate : String,
    val endDate : String,
    val description : String,
    val assignedTo : String,
    val assigneeGroup : String,
    val progress : Int = 0
) : Parcelable