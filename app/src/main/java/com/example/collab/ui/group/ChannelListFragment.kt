package com.example.collab.ui.group

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
import com.example.collab.core.data.ChannelData
import com.example.collab.core.data.GroupData
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData
import com.example.collab.core.ui.ChannelListAdapter
import com.example.collab.core.viewModel.GroupViewModel
import com.example.collab.databinding.FragmentChannelListBinding
import com.example.collab.ui.CreateChannelActivity

class ChannelListFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentChannelListBinding : FragmentChannelListBinding
    private lateinit var groupViewModel : GroupViewModel
    private var getChannelsData : GroupData? = null
    private var channelsId = ArrayList<String>()
    private var channelListData = ArrayList<ChannelData>()
    private val channelListAdapter = ChannelListAdapter()
    private var groupId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentChannelListBinding = FragmentChannelListBinding.inflate(inflater, container, false)
        return fragmentChannelListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentChannelListBinding.createChannelButton.setOnClickListener(this)

        getChannelsData = activity?.intent?.getParcelableExtra("GROUP_DATA")
        groupId = getChannelsData!!.groupId
        val userData = activity?.intent?.getParcelableExtra<UserData>("USER_DATA_SENDER")

        val factory = ViewModelFactory.getInstance()
        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)
        if (userData != null)
        {
            channelList(userData)
        }

        with(fragmentChannelListBinding.channelsRecyclerview)
        {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = channelListAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.create_channel_button -> {
                val createChannelIntent = Intent(activity, CreateChannelActivity::class.java)
                createChannelIntent.putExtra("CHANNEL_GROUP_ID", getChannelsData?.groupId)
                startActivity(createChannelIntent)
            }
        }
    }

    private fun channelList(userData : UserData)
    {
        if (RemoteDataSource().isOnline(requireContext()))
        {
            groupViewModel.getChannelListData(groupId).observe(viewLifecycleOwner) {
                channelsId = ArrayList(it)
                groupViewModel.getChannelNameData(channelsId).observe(viewLifecycleOwner) {
                    channelListData = ArrayList(it)
                    channelListAdapter.setChannelData(channelListData, userData)
                    channelListAdapter.notifyDataSetChanged()
                }
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Koneksi internet tidak aktif", Toast.LENGTH_SHORT).show()
        }
    }
}