package com.example.collab.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.JoinGroupViewModel
import com.example.collab.databinding.ActivityJoinGroupBinding
import com.google.android.material.snackbar.Snackbar

class JoinGroupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityJoinGroupBinding : ActivityJoinGroupBinding
    private lateinit var joinGroupViewModel : JoinGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityJoinGroupBinding = ActivityJoinGroupBinding.inflate(layoutInflater)
        setContentView(activityJoinGroupBinding.root)

        val getGroupName = intent.getStringExtra("GROUP_NAME")!!

        setSupportActionBar(activityJoinGroupBinding.joinGroupToolbar)
        title = "Bergabung ke Grup $getGroupName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityJoinGroupBinding.joinGroupButton.setOnClickListener(this)

        val factory = ViewModelFactory.getInstance()
        joinGroupViewModel = ViewModelProvider(this, factory).get(JoinGroupViewModel::class.java)
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
            R.id.join_group_button -> {
                validateJoinGroup(v)
            }
        }
    }

    private fun validateJoinGroup(v : View)
    {
        val groupIdInput = activityJoinGroupBinding.joinGroupId.text.toString()
        val groupPasswordInput = activityJoinGroupBinding.joinGroupPassword.text.toString()

        if (groupIdInput.isEmpty() || groupPasswordInput.isEmpty())
        {
            Snackbar.make(v, "Mohon isi semua kolom yang tersedia!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!groupIdInput.matches("[a-zA-Z0-9]+".toRegex()))
        {
            Snackbar.make(v, "Id grup hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!groupPasswordInput.matches("[a-zA-Z0-9]+".toRegex()))
        {
            Snackbar.make(v, "Password grup hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            checkJoinGroup(groupIdInput, groupPasswordInput, v)
        }
    }

    private fun checkJoinGroup(groupId : String, groupPassword : String, v : View)
    {
        val getMemberData = intent.getParcelableExtra<UserData>("MEMBER_DATA")

        joinGroupViewModel.getJoinGroupData().observe(this) {
            if (groupId != it.groupId)
            {
                Snackbar.make(v, "Id grup salah!", Snackbar.LENGTH_SHORT).show()
            }
            else if (groupPassword != it.groupPassword)
            {
                Snackbar.make(v, "Password grup salah!", Snackbar.LENGTH_SHORT).show()
            }
            else
            {
                saveJoinedMember(getMemberData!!, groupId, v)
            }
        }
    }

    private fun saveJoinedMember(member : UserData, groupId : String, v : View)
    {
        joinGroupViewModel.setSaveJoinGroup(member, groupId)
        Snackbar.make(v, "Berhasil bergabung ke grup!", Snackbar.LENGTH_SHORT).show()
    }
}
