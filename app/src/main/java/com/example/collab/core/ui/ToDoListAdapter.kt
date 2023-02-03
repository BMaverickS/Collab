package com.example.collab.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.R
import com.example.collab.core.data.ToDoData
import com.example.collab.databinding.ToDoRowBinding
import com.example.collab.ui.ToDoDetailsActivity
import java.text.SimpleDateFormat
import java.util.*

class ToDoListAdapter : RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder>() {

    private var toDoList = ArrayList<ToDoData>()
    private var groupName = ArrayList<String>()
    private var groupId = ArrayList<String>()

    fun setToDoData(items : ArrayList<ToDoData>, grName : ArrayList<String>, grId : ArrayList<String>)
    {
        toDoList.clear()
        toDoList.addAll(items)
        groupName.addAll(grName)
        groupId.addAll(grId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val toDoRowBinding = ToDoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(toDoRowBinding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val toDo = toDoList[position]
        holder.bind(toDo,groupName, groupId)
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    class ToDoViewHolder(private val binding : ToDoRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData : ToDoData, groupName : ArrayList<String>, groupId : ArrayList<String>)
        {
            val checkDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Calendar.getInstance().time)
            val frmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)

            val tdDate = toDoData.beginDate + " - " + toDoData.endDate
            val prgs = toDoData.progress.toString() + "%"
            binding.toDoTitleItem.text = toDoData.toDoTitle
            binding.assignedNameItem.text = toDoData.assignedTo
            binding.assigneeGroupItem.text = toDoData.assigneeGroup
            binding.toDoDateItem.text = tdDate
            binding.progressColorItem.background.setTint(
                if (toDoData.endDate != "")
                {
                    if (toDoData.progress < 100 && frmt.parse(toDoData.endDate)!! < frmt.parse(checkDate))
                    {
                        ContextCompat.getColor(itemView.context, R.color.warning_red)
                    }
                    else if (toDoData.progress == 100)
                    {
                        ContextCompat.getColor(itemView.context, R.color.primary_green)
                    }
                    else
                    {
                        ContextCompat.getColor(itemView.context, R.color.blue_primary)
                    }
                }
                else
                {
                    ContextCompat.getColor(itemView.context, R.color.blue_primary)
                })
            binding.progressItem.text = prgs
            itemView.setOnClickListener {
                val toDoPageIntent = Intent(itemView.context, ToDoDetailsActivity::class.java)
                toDoPageIntent.putExtra("TO_DO_DATA", toDoData)
                toDoPageIntent.putExtra("GROUP_NAME_LIST", groupName)
                toDoPageIntent.putExtra("GROUP_ID_LIST", groupId)
                itemView.context.startActivity(toDoPageIntent)
            }
        }
    }
}