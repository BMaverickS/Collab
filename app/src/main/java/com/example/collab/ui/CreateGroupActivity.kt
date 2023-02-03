package com.example.collab.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.MemberData
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.CreateGroupViewModel
import com.example.collab.databinding.ActivityCreateGroupBinding
import com.google.android.material.snackbar.Snackbar

class CreateGroupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityCreateGroupBinding : ActivityCreateGroupBinding
    private lateinit var createGroupViewModel : CreateGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCreateGroupBinding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(activityCreateGroupBinding.root)

        setSupportActionBar(activityCreateGroupBinding.createGroupToolbar)
        title = "Buat Grup"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityCreateGroupBinding.createGroupButton.setOnClickListener(this)

        val factory = ViewModelFactory.getInstance()
        createGroupViewModel = ViewModelProvider(this, factory).get(CreateGroupViewModel::class.java)
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
            R.id.create_group_button -> {
                validateCreateGroup(v)
            }
        }
    }

    private fun validateCreateGroup(v : View)
    {
        val groupNameInput = activityCreateGroupBinding.createGroupName.text.toString()
        val groupIdInput = activityCreateGroupBinding.createGroupId.text.toString()
        val groupPasswordInput = activityCreateGroupBinding.createGroupPassword.text.toString()

        if (groupNameInput.isEmpty() || groupIdInput.isEmpty() || groupPasswordInput.isEmpty())
        {
            Snackbar.make(v, "Mohon isi semua kolom yang tersedia!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!groupNameInput.matches("[a-zA-Z0-9 ]+".toRegex()))
        {
            Snackbar.make(v, "Nama grup hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (groupNameInput.length < 5 || groupNameInput.length > 25)
        {
            Snackbar.make(v, "Panjang nama grup harus 5 - 25 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!groupIdInput.matches("[a-zA-Z0-9]+".toRegex()))
        {
            Snackbar.make(v, "Id grup hanya boleh terdiri dari huruf kecil, huruf kapital, angka dan tidak boleh ada spasi!", Snackbar.LENGTH_SHORT).show()
        }
        else if (groupIdInput.length < 6 || groupIdInput.length > 15)
        {
            Snackbar.make(v, "Panjang id grup harus 6 - 15 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!groupPasswordInput.matches("[a-zA-Z0-9]+".toRegex()))
        {
            Snackbar.make(v, "Password grup hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (groupPasswordInput.length < 4 || groupPasswordInput.length > 20)
        {
            Snackbar.make(v, "Panjang id grup harus 4 - 20 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            checkCreateGroup(groupIdInput, groupNameInput, groupPasswordInput, v)
        }
    }

    private fun checkCreateGroup(groupId : String, groupName : String, groupPassword : String, v : View)
    {
        createGroupViewModel.getGroupData(groupName, groupId).observe(this) {
            if (it.size == 2)
            {
                if (it[0] == true)
                {
                    Snackbar.make(v, "Nama grup telah digunakan!", Snackbar.LENGTH_SHORT).show()
                }
                else if (it[1] == true)
                {
                    Snackbar.make(v, "ID grup telah digunakan!", Snackbar.LENGTH_SHORT).show()
                }
                else
                {
                    saveGroup(groupId, groupName, groupPassword, v)
                }
            }
        }
    }

    private fun saveGroup(groupId : String, groupName : String, groupPassword : String, v : View)
    {
        val getMemberData = intent.getParcelableExtra<UserData>("MEMBER_DATA")

        val member = ArrayList<MemberData>()
        member.add(MemberData(getMemberData?.userId!!, getMemberData.username, "Pemimpin tim"))
        val createGroupData = GroupData(groupId, groupName, groupPassword, null, member, null)
        createGroupViewModel.setCreateGroup(createGroupData)
        Snackbar.make(v, "Grup berhasil dibuat!", Snackbar.LENGTH_SHORT).show()
    }
}