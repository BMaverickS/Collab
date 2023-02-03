package com.example.collab.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.core.data.MemberData
import com.example.collab.databinding.MemberRowBinding
import com.example.collab.ui.MemberDetailsActivity

class MemberListAdapter : RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {

    private var memberList = ArrayList<MemberData>()
    private var groupId = ""

    fun setMemberData(items : ArrayList<MemberData>?, grId : String)
    {
        memberList.clear()
        if (items != null)
        {
            memberList = items
        }
        groupId = grId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val memberRowBinding = MemberRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(memberRowBinding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = memberList[position]
        holder.bind(member, groupId, memberList.size)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class MemberViewHolder(private val binding : MemberRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberData : MemberData, groupId : String, j : Int)
        {
            binding.memberNameItem.text = memberData.memberName
            binding.memberStatusItem.text = memberData.memberStatus
            itemView.setOnClickListener {
                val memberDetailsIntent = Intent(itemView.context, MemberDetailsActivity::class.java)
                memberDetailsIntent.putExtra("MEMBER_DETAILS_DATA", memberData)
                memberDetailsIntent.putExtra("MEMBER_GROUP_ID", groupId)
                memberDetailsIntent.putExtra("TOTAL_MEMBER", j)
                itemView.context.startActivity(memberDetailsIntent)
            }
        }
    }
}