package com.example.collab.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData
import com.example.collab.core.ui.GroupListAdapter
import com.example.collab.core.viewModel.HomeViewModel
import com.example.collab.databinding.FragmentHomeBinding
import com.example.collab.ui.CreateGroupActivity
import com.example.collab.ui.JoinGroupActivity
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentHomeBinding : FragmentHomeBinding
    private lateinit var homeViewModel : HomeViewModel
    private var getUserData : UserData? = null
    private val groupListAdapter = GroupListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHomeBinding.searchGroupImage.setOnClickListener(this)
        fragmentHomeBinding.createGroupButton.setOnClickListener(this)

        getUserData = activity?.intent?.getParcelableExtra("USER_DATA")
        val getUserId = getUserData?.userId

        val factory = ViewModelFactory.getInstance()
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        if (getUserId != null)
        {
            groupList(getUserId)
        }

        with(fragmentHomeBinding.groupRecyclerview)
        {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = groupListAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.search_group_image -> {
                searchGroup(v)
            }

            R.id.create_group_button -> {
                val createGroupIntent = Intent(activity, CreateGroupActivity::class.java)
                createGroupIntent.putExtra("MEMBER_DATA", getUserData)
                startActivity(createGroupIntent)
            }
        }
    }

    private fun searchGroup(v : View)
    {
        val input = fragmentHomeBinding.searchGroup.text.toString()
        homeViewModel.getSearchGroupData(input).observe(this) {
            if (it == true)
            {
                val searchGroupIntent = Intent(activity, JoinGroupActivity::class.java)
                searchGroupIntent.putExtra("MEMBER_DATA", getUserData)
                searchGroupIntent.putExtra("GROUP_NAME", input)
                startActivity(searchGroupIntent)
            }
            else
            {
                Snackbar.make(v, "Nama grup tidak ditemukan!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun groupList(getUserId : String)
    {
        if (RemoteDataSource().isOnline(requireContext()))
        {
            homeViewModel.getUserData(getUserId).observe(viewLifecycleOwner) {
                homeViewModel.getGroupListData(it.groupId!!).observe(viewLifecycleOwner) {
                    groupListAdapter.setGroupData(ArrayList(it), getUserData!!)
                    groupListAdapter.notifyDataSetChanged()
                }
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Koneksi internet tidak aktif", Toast.LENGTH_SHORT).show()
        }
    }
}
