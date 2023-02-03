package com.example.collab.ui.todo

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
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData
import com.example.collab.core.ui.ToDoListAdapter
import com.example.collab.core.viewModel.ToDoViewModel
import com.example.collab.databinding.FragmentToDoBinding
import com.example.collab.ui.CreateToDoActivity
import com.google.android.material.snackbar.Snackbar

class ToDoFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentToDoBinding : FragmentToDoBinding
    private lateinit var toDoViewModel : ToDoViewModel
    private var toDoId = ArrayList<String>()
    private var tmpName = ArrayList<String>()
    private var tmpId = ArrayList<String>()
    private val toDoListAdapter = ToDoListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentToDoBinding = FragmentToDoBinding.inflate(inflater, container, false)
        return fragmentToDoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentToDoBinding.createToDoButton.setOnClickListener(this)

        val getGroupId = activity?.intent?.getParcelableExtra<UserData>("USER_DATA")?.groupId
        val getMemberId = activity?.intent?.getParcelableExtra<UserData>("USER_DATA")?.userId

        val factory = ViewModelFactory.getInstance()
        toDoViewModel = ViewModelProvider(this, factory).get(ToDoViewModel::class.java)

        if (getGroupId != null && getMemberId != null)
        {
            RemoteDataSource().getUserData(getMemberId).observe(viewLifecycleOwner) {
                toDoViewModel.getGroupMemberData(getGroupId, getMemberId).observe(viewLifecycleOwner) {
                    if (it.isNotEmpty())
                    {
                        fragmentToDoBinding.createToDoButton.visibility = View.VISIBLE
                    }
                    else
                    {
                        fragmentToDoBinding.createToDoButton.visibility = View.GONE
                    }
                }

                toDoList(it.groupId!!, view)
            }
        }

        with(fragmentToDoBinding.toDoRecyclerview)
        {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = toDoListAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.create_to_do_button -> {
                val createToDoIntent = Intent(context, CreateToDoActivity::class.java)
                createToDoIntent.putStringArrayListExtra("GROUP_NAME_DATA", tmpName)
                createToDoIntent.putStringArrayListExtra("GROUP_ID_DATA", tmpId)
                createToDoIntent.putExtra("TO_DO_USER_ID", activity?.intent?.getParcelableExtra<UserData>("USER_DATA")?.userId)
                startActivity(createToDoIntent)
            }
        }
    }

    private fun toDoList(getGroupId : ArrayList<String>, v : View)
    {
        var temp : String
        toDoViewModel.getGroupListData(getGroupId).observe(viewLifecycleOwner) {
            tmpName.clear()
            tmpId.clear()
            it.forEach {
                it.toDoId?.forEach {
                    temp = it
                    toDoId.add(temp)
                }
                tmpName.add(it.groupName)
                tmpId.add(it.groupId)
            }
            if (toDoId.isEmpty())
            {
                Snackbar.make(v, "Tidak ada to do!", Snackbar.LENGTH_SHORT).show()
            }
            else
            {
                toDoViewModel.getToDoListData(toDoId).observe(viewLifecycleOwner) {
                    toDoListAdapter.setToDoData(ArrayList(it), tmpName, tmpId)
                    toDoListAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}