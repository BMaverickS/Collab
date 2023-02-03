package com.example.collab.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupData (
    val groupId : String,
    val groupName : String,
    val groupPassword : String,
    val channelId : ArrayList<String>?,
    val member : ArrayList<MemberData>?,
    val toDoId : ArrayList<String>?
) : Parcelable

@Parcelize
data class MemberData (
    val memberId : String,
    val memberName : String,
    val memberStatus : String
) : Parcelable

// val : assign once, immutable
// var : assign multiple time, mutable