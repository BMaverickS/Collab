package com.example.collab.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.UserData
import com.example.collab.databinding.GroupNameRowBinding
import com.example.collab.ui.GroupActivity

class GroupListAdapter : RecyclerView.Adapter<GroupListAdapter.GroupViewHolder>() {

    private val groupList = ArrayList<GroupData>()
    private var userData : UserData? = null

    fun setGroupData(items : ArrayList<GroupData>, usData : UserData)
    {
        groupList.clear()
        groupList.addAll(items)
        userData = usData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val groupNameRowBinding = GroupNameRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(groupNameRowBinding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList[position]
        holder.bind(group, userData!!)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class GroupViewHolder(private val binding : GroupNameRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupData : GroupData, userData : UserData)
        {
            binding.groupNameItem.text = groupData.groupName
            itemView.setOnClickListener {
                val groupPageIntent = Intent(itemView.context, GroupActivity::class.java)
                groupPageIntent.putExtra("GROUP_DATA", groupData)
                groupPageIntent.putExtra("USER_DATA_SENDER", userData)
                itemView.context.startActivity(groupPageIntent)
            }
        }
    }
}

// putting click listener inside bind() will make setOnClickListener called once