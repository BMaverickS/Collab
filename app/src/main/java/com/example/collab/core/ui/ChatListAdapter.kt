package com.example.collab.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.core.data.ChatData
import com.example.collab.databinding.ChatRowBinding

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    private val chatList = ArrayList<ChatData>()

    fun setChatData(items : ArrayList<ChatData>)
    {
        chatList.clear()
        chatList.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val chatRowBinding = ChatRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(chatRowBinding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatViewHolder(private val binding : ChatRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatData : ChatData)
        {
            binding.chatSenderItem.text = chatData.messageSender
            binding.chatMessageItem.text = chatData.message
        }
    }
}