package com.example.collab.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.ChatData
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData
import com.example.collab.core.ui.ChatListAdapter
import com.example.collab.core.viewModel.ChatViewModel
import com.example.collab.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityChatBinding : ActivityChatBinding
    private lateinit var chatViewModel : ChatViewModel
    private var getChannelId = ""
    private var getUserData : UserData? = null
    private var chat = ArrayList<ChatData>()
    private val chatListAdapter = ChatListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(activityChatBinding.root)

        val getChannelName = intent.getStringExtra("CHANNEL_NAME")
        setSupportActionBar(activityChatBinding.chatToolbar)
        title = getChannelName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getChannelId = intent.getStringExtra("CHANNEL_ID")!!
        getUserData = intent.getParcelableExtra("CHAT_USER_DATA")!!

        activityChatBinding.sendMessageImage.setOnClickListener(this)

        val factory = ViewModelFactory.getInstance()
        chatViewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        chatList()

        with(activityChatBinding.chatRecyclerview)
        {
            val lManager = LinearLayoutManager(context)
            lManager.stackFromEnd = true
            layoutManager = lManager
            setHasFixedSize(true)
            this.adapter = chatListAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.send_message_image -> {
                sendMessage()
            }
        }
    }

    private fun chatList()
    {
        if (RemoteDataSource().isOnline(this))
        {
            chatViewModel.getChatData(getChannelId).observe(this) {
                chatListAdapter.setChatData(ArrayList(it))
                chatListAdapter.notifyDataSetChanged()
                chat = ArrayList(it)

                activityChatBinding.chatRecyclerview.post {activityChatBinding.chatRecyclerview.scrollToPosition(chat.size - 1)}
            }
        }
        else
        {
            Toast.makeText(this, "Koneksi internet tidak aktif", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage()
    {
        val messageText = activityChatBinding.sendMessage.text.toString()
        if (messageText.isNotEmpty())
        {
            chatViewModel.setChatMessage(getChannelId, ChatData(getUserData?.username!!, messageText))
            activityChatBinding.sendMessage.setText("")
        }
    }
}