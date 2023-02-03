package com.example.collab.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData (
    val userId : String,
    val username : String,
    var email : String,
    val password : String,
    var groupId : ArrayList<String>?
) : Parcelable
