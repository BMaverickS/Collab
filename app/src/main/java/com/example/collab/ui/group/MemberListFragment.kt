package com.example.collab.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.GroupData
import com.example.collab.core.ui.MemberListAdapter
import com.example.collab.core.viewModel.GroupViewModel
import com.example.collab.databinding.FragmentMemberListBinding
import com.example.collab.ui.AddMemberActivity

class MemberListFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentMemberListBinding : FragmentMemberListBinding
    private lateinit var groupViewModel : GroupViewModel
    private var getMembersData : GroupData? = null
    private val membersListAdapter = MemberListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentMemberListBinding = FragmentMemberListBinding.inflate(inflater, container, false)
        return fragmentMemberListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentMemberListBinding.addMemberButton.setOnClickListener(this)

        getMembersData = activity?.intent?.getParcelableExtra("GROUP_DATA")

        val factory = ViewModelFactory.getInstance()
        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)
        memberList()

        with(fragmentMemberListBinding.membersRecyclerview)
        {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = membersListAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.add_member_button -> {
                val addMemberIntent = Intent(activity, AddMemberActivity::class.java)
                addMemberIntent.putExtra("MEMBER_GROUP_ID", getMembersData?.groupId)
                startActivity(addMemberIntent)
            }
        }
    }

    private fun memberList()
    {
        groupViewModel.getMemberListData(getMembersData!!.groupId).observe(viewLifecycleOwner) {
            membersListAdapter.setMemberData(ArrayList(it), getMembersData!!.groupId)
            membersListAdapter.notifyDataSetChanged()
        }
    }
}