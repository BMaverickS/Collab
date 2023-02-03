package com.example.collab.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.AddMemberViewModel
import com.example.collab.databinding.ActivityAddMemberBinding
import com.google.android.material.snackbar.Snackbar

class AddMemberActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityAddMemberBinding : ActivityAddMemberBinding
    private lateinit var addMemberViewModel : AddMemberViewModel
    private var getGroupId = ""
    private var memberData : UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddMemberBinding = ActivityAddMemberBinding.inflate(layoutInflater)
        setContentView(activityAddMemberBinding.root)

        setSupportActionBar(activityAddMemberBinding.addMemberToolbar)
        title = "Tambah Anggota"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityAddMemberBinding.searchMemberButton.setOnClickListener(this)
        activityAddMemberBinding.addMemberButton.setOnClickListener(this)

        val factory = ViewModelFactory.getInstance()
        addMemberViewModel = ViewModelProvider(this, factory).get(AddMemberViewModel::class.java)

        getGroupId = intent.getStringExtra("MEMBER_GROUP_ID")!!
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
            R.id.search_member_button -> {
                searchMember(v)
            }

            R.id.add_member_button -> {
                saveSearchedMember(v)
            }
        }
    }

    private fun searchMember(v : View)
    {
        val memberIdInput = activityAddMemberBinding.searchMemberId.text.toString()

        if (memberIdInput.isEmpty())
        {
            Snackbar.make(v, "Id pengguna belum diisi!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            addMemberViewModel.getSearchMemberData(memberIdInput).observe(this) {
                if (it.username.isEmpty())
                {
                    activityAddMemberBinding.searchMemberResult.text = ""
                    memberData = null
                    Snackbar.make(v, "Id pengguna tidak ditemukan!", Snackbar.LENGTH_SHORT).show()
                }
                else
                {
                    activityAddMemberBinding.searchMemberResult.text = it.username
                    memberData = it
                }
            }
        }
    }

    private fun saveSearchedMember(v : View)
    {
        if (memberData != null)
        {
            addMemberViewModel.setSaveMember(memberData!!, getGroupId)
            Snackbar.make(v, "Pengguna berhasil ditambahkan ke grup!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            Snackbar.make(v, "Data pengguna tidak ditemukan!", Snackbar.LENGTH_SHORT).show()
        }
    }
}