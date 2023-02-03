package com.example.collab.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.data.ToDoData
import com.example.collab.core.viewModel.CreateToDoViewModel
import com.example.collab.databinding.ActivityCreateToDoBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class CreateToDoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityCreateToDoBinding : ActivityCreateToDoBinding
    private lateinit var createToDoViewModel : CreateToDoViewModel
    private var getBeginDate = ""
    private var getEndDate = ""
    private var getGroupName = ArrayList<String>()
    private var getGroupId = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCreateToDoBinding = ActivityCreateToDoBinding.inflate(layoutInflater)
        setContentView(activityCreateToDoBinding.root)

        setSupportActionBar(activityCreateToDoBinding.createToDoToolbar)
        title = "Buat To Do"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityCreateToDoBinding.createToDoBeginDate.setOnClickListener(this)
        activityCreateToDoBinding.createToDoEndDate.setOnClickListener(this)
        activityCreateToDoBinding.createToDoButton.setOnClickListener(this)

        createToDoViewModel = ViewModelProvider(this).get(CreateToDoViewModel::class.java)

        getGroupName = intent.getStringArrayListExtra("GROUP_NAME_DATA")!!
        getGroupId = intent.getStringArrayListExtra("GROUP_ID_DATA")!!
        //println("gr name : $getGroupName  ,  gr id : $getGroupId")
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
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val checkDate = "" + day + "/" + (month + 1) + "/" + year
        val frmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        when (v?.id)
        {
            R.id.create_to_do_begin_date -> {
                val datePickerDialog = DatePickerDialog(this, {
                        view, bYear, bMonth, bDay ->
                    getBeginDate = "$bDay/${bMonth + 1}/$bYear"
                    if (frmt.parse(getBeginDate)!! <= frmt.parse(checkDate))
                    {
                        activityCreateToDoBinding.createToDoBeginDate.setText(getBeginDate)
                    }
                    else
                    {
                        getBeginDate = ""
                        Snackbar.make(v, "Tanggal mulai tidak boleh melewati tanggal hari ini!", Snackbar.LENGTH_SHORT).show()
                    }
                }, year, month, day)
                datePickerDialog.show()
            }

            R.id.create_to_do_end_date -> {
                val datePickerDialog = DatePickerDialog(this, {
                        view, eYear, eMonth, eDay ->
                    getEndDate = "$eDay/${eMonth + 1}/$eYear"
                    if (getBeginDate != "")
                    {
                        if (frmt.parse(getEndDate)!! >= frmt.parse(getBeginDate))
                        {
                            activityCreateToDoBinding.createToDoEndDate.setText(getEndDate)
                        }
                        else
                        {
                            getEndDate = ""
                            Snackbar.make(v, "Tanggal berakhir tidak boleh sebelum tanggal mulai!", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        getEndDate = ""
                        Snackbar.make(v, "Mohon isi tanggal mulai terlebih dahulu!", Snackbar.LENGTH_SHORT).show()
                    }
                }, year, month, day)
                datePickerDialog.show()
            }

            R.id.create_to_do_button -> {
                validateCreateToDo(v)
            }
        }
    }

    private fun validateCreateToDo(v : View)
    {
        val titleInput = activityCreateToDoBinding.createToDoTitle.text.toString()
        val beginDateInput = activityCreateToDoBinding.createToDoBeginDate.text.toString()
        val endDateInput = activityCreateToDoBinding.createToDoEndDate.text.toString()
        val descriptionInput = activityCreateToDoBinding.createToDoDescription.text.toString()
        val assignedToInput = activityCreateToDoBinding.createToDoAssignedTo.text.toString()
        val assigneeGroupInput = activityCreateToDoBinding.createToDoAssigneeGroup.text.toString()

        //println("contains : $getGroupName  ||  $assigneeGroupInput")

        if (titleInput.isEmpty() || beginDateInput.isEmpty() || endDateInput.isEmpty() || descriptionInput.isEmpty() ||
            assignedToInput.isEmpty() || assigneeGroupInput.isEmpty())
        {
            Snackbar.make(v, "Mohon isi semua kolom yang tersedia!", Snackbar.LENGTH_SHORT).show()
        }
        else if (titleInput.length < 5 || titleInput.length > 25)
        {
            Snackbar.make(v, "Panjang judul harus 5 - 25 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (descriptionInput.length > 250)
        {
            Snackbar.make(v, "Deskripsi maksimal 250 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!assignedToInput.matches("[a-zA-Z0-9 ]+".toRegex()))
        {
            Snackbar.make(v, "Username hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (assignedToInput.length < 5 || assignedToInput.length > 25)
        {
            Snackbar.make(v, "Panjang username harus 5 - 25 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!assigneeGroupInput.matches("[a-zA-Z0-9 ]+".toRegex()))
        {
            Snackbar.make(v, "Nama grup hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (assigneeGroupInput.length < 5 || assigneeGroupInput.length > 25)
        {
            Snackbar.make(v, "Panjang nama grup harus 5 - 25 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!getGroupName.contains(assigneeGroupInput))
        {
            Snackbar.make(v, "Pengguna bukan bagian dari grup tersebut!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            val saveGroupId = getGroupId[getGroupName.indexOf(assigneeGroupInput)]
            val userId = intent.getStringExtra("TO_DO_USER_ID")
            if (userId != null)
            {
                createToDoViewModel.getGroupMemberData(getGroupId, userId).observe(this) {
                    val memberIdList = ArrayList<String>()
                    it.forEach {
                        memberIdList.add(it.memberId)
                    }
                    if (memberIdList.contains(saveGroupId))
                    {
                        saveCreatedToDo(titleInput, beginDateInput, endDateInput, descriptionInput, assignedToInput, assigneeGroupInput, v)
                    }
                    else
                    {
                        Snackbar.make(v, "Anda tidak memiliki izin untuk membuat to do di grup tersebut!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveCreatedToDo(titleInput : String, beginDateInput : String, endDateInput : String,
                        descriptionInput : String, assignedToInput : String, assigneeGroupInput : String, v : View)
    {
        val saveGroupId = getGroupId[getGroupName.indexOf(assigneeGroupInput)]
        val getToDoData = ToDoData("", titleInput, beginDateInput, endDateInput, descriptionInput, assignedToInput, assigneeGroupInput, 0)
        createToDoViewModel.setCreateToDo(getToDoData, saveGroupId)
        Snackbar.make(v, "To do berhasil dibuat!", Snackbar.LENGTH_SHORT).show()
    }
}