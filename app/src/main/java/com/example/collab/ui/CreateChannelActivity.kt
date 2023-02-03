package com.example.collab.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.viewModel.CreateChannelViewModel
import com.example.collab.databinding.ActivityCreateChannelBinding
import com.google.android.material.snackbar.Snackbar

class CreateChannelActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityCreateChannelBinding : ActivityCreateChannelBinding
    private lateinit var createChannelViewModel : CreateChannelViewModel
    private var groupId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCreateChannelBinding = ActivityCreateChannelBinding.inflate(layoutInflater)
        setContentView(activityCreateChannelBinding.root)

        setSupportActionBar(activityCreateChannelBinding.createChannelToolbar)
        title = "Buat Channel"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityCreateChannelBinding.createChannelButton.setOnClickListener(this)

        groupId = intent.getStringExtra("CHANNEL_GROUP_ID")!!

        createChannelViewModel = ViewModelProvider(this).get(CreateChannelViewModel::class.java)
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
            R.id.create_channel_button -> {
                validateCreateChannel(v)
            }
        }
    }

    private fun validateCreateChannel(v : View)
    {
        val channelNameInput = activityCreateChannelBinding.createChannelName.text.toString()

        if (channelNameInput.isEmpty())
        {
            Snackbar.make(v, "Mohon isi nama channel!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!channelNameInput.matches("[a-zA-Z0-9 ]+".toRegex()))
        {
            Snackbar.make(v, "Nama channel hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (channelNameInput.length < 5 || channelNameInput.length > 25)
        {
            Snackbar.make(v, "Panjang nama channel harus 5 - 25 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            saveCreatedChannel(channelNameInput, v)
        }
    }

    private fun saveCreatedChannel(channelName : String, v : View)
    {
        createChannelViewModel.setChannel(groupId, channelName)
        Snackbar.make(v, "Channel berhasil dibuat!", Snackbar.LENGTH_SHORT).show()
    }
}