package com.example.collab.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.core.data.ChannelData
import com.example.collab.core.data.UserData
import com.example.collab.databinding.GroupNameRowBinding
import com.example.collab.ui.ChatActivity

class ChannelListAdapter : RecyclerView.Adapter<ChannelListAdapter.ChannelViewHolder>() {

    private val channelList = ArrayList<ChannelData>()
    private var userData : UserData? = null

    fun setChannelData(items : ArrayList<ChannelData>, usData : UserData)
    {
        channelList.clear()
        channelList.addAll(items)
        userData = usData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val groupNameRowBinding = GroupNameRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChannelViewHolder(groupNameRowBinding)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = channelList[position]
        holder.bind(channel.channelName, channel.channelId, userData!!)
    }

    override fun getItemCount(): Int {
        return channelList.size
    }

    class ChannelViewHolder(private val binding : GroupNameRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(channelName : String, channelId : String, userData : UserData)
        {
            binding.groupNameItem.text = channelName
            itemView.setOnClickListener {
                val chatPageIntent = Intent(itemView.context, ChatActivity::class.java)
                chatPageIntent.putExtra("CHANNEL_ID", channelId)
                chatPageIntent.putExtra("CHANNEL_NAME", channelName)
                chatPageIntent.putExtra("CHAT_USER_DATA", userData)
                itemView.context.startActivity(chatPageIntent)
            }
        }
    }
}