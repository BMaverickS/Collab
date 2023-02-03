package com.example.collab.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelData (
    val channelId : String,
    val channelName : String,
    val chat : ArrayList<ChatData>
) : Parcelable

@Parcelize
data class ChatData (
    val messageSender : String,
    val message : String,
) : Parcelable