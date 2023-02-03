package com.example.collab.ui

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.GroupViewModel
import com.example.collab.databinding.ActivityGroupBinding
import com.example.collab.ui.group.SectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class GroupActivity : AppCompatActivity() {

    private lateinit var activityGroupBinding : ActivityGroupBinding
    private lateinit var groupViewModel : GroupViewModel
    private val list = ArrayList<String>()
    private var getGroupData : GroupData? = null
    private var getUserId = ""
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityGroupBinding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(activityGroupBinding.root)

        getGroupData = intent.getParcelableExtra("GROUP_DATA")
        getUserId = intent.getParcelableExtra<UserData>("USER_DATA_SENDER")!!.userId

        activityGroupBinding.groupToolbar.inflateMenu(R.menu.group_delete_menu)
        view = activityGroupBinding.root

        setSupportActionBar(activityGroupBinding.groupToolbar)
        title = getGroupData?.groupName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list.add("Channel")
        list.add("Anggota")

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = activityGroupBinding.groupViewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = activityGroupBinding.groupTabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = list[position]
        }.attach()

        supportActionBar?.elevation = 0f

        val factory = ViewModelFactory.getInstance()
        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.group_delete_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.delete_group -> {
                deleteGroup()
            }

            R.id.delete_channel -> {
                deleteChannel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteGroup()
    {
        groupViewModel.setDeleteGroup(getGroupData!!.groupId, getUserId)
        Snackbar.make(view, "Grup berhasil dihapus!", Snackbar.LENGTH_SHORT).show()
        Handler(mainLooper).postDelayed({
            finish()
        }, 1000)
    }

    private fun deleteChannel()
    {
        groupViewModel.setDeleteChannel(getGroupData!!.groupId)
        Snackbar.make(view, "Channel berhasil dihapus!", Snackbar.LENGTH_SHORT).show()
    }
}

// companion object : to declare an object inside a class. Class members can access the
// private members of the corresponding companion object.4