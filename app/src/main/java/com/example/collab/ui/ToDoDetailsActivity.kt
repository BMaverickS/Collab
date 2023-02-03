package com.example.collab.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.ToDoData
import com.example.collab.core.viewModel.ToDoDetailsViewModel
import com.example.collab.databinding.ActivityToDoDetailsBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class ToDoDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityToDoDetailsBinding : ActivityToDoDetailsBinding
    private lateinit var toDoDetailsViewModel : ToDoDetailsViewModel
    private var getBeginDate = ""
    private var getEndDate = ""
    private var getGroupName = ArrayList<String>()
    private var getGroupId = ArrayList<String>()
    private var getToDoData : ToDoData? = null
    private var toDoData : ToDoData? = null
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityToDoDetailsBinding = ActivityToDoDetailsBinding.inflate(layoutInflater)
        setContentView(activityToDoDetailsBinding.root)

        setSupportActionBar(activityToDoDetailsBinding.detailsToDoToolbar)
        title = "Detil To Do"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activityToDoDetailsBinding.detailsToDoToolbar.inflateMenu(R.menu.delete_to_do_menu)
        view = activityToDoDetailsBinding.root

        activityToDoDetailsBinding.detailsToDoBeginDate.setOnClickListener(this)
        activityToDoDetailsBinding.detailsToDoEndDate.setOnClickListener(this)
        activityToDoDetailsBinding.detailsToDoSaveButton.setOnClickListener(this)

        getToDoData = intent.getParcelableExtra("TO_DO_DATA")
        getGroupId = intent.getStringArrayListExtra("GROUP_ID_LIST")!!
        getGroupName = intent.getStringArrayListExtra("GROUP_NAME_LIST")!!

        toDoDetailsViewModel = ViewModelProvider(this).get(ToDoDetailsViewModel::class.java)

        toDoDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.delete_to_do_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.delete_to_do -> {
                deleteToDo()
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
            R.id.details_to_do_begin_date -> {
                val datePickerDialog = DatePickerDialog(this, {
                        view, bYear, bMonth, bDay ->
                    getBeginDate = "" + bDay + "/" + (bMonth + 1) + "/" + bYear
                    if (frmt.parse(getBeginDate)!! <= frmt.parse(checkDate))
                    {
                        activityToDoDetailsBinding.detailsToDoBeginDate.setText(getBeginDate)
                    }
                    else
                    {
                        Snackbar.make(v, "Tanggal mulai tidak boleh melewati tanggal hari ini!", Snackbar.LENGTH_SHORT).show()
                    }
                }, year, month, day)
                datePickerDialog.show()
            }

            R.id.details_to_do_end_date -> {
                val datePickerDialog = DatePickerDialog(this, {
                        view, eYear, eMonth, eDay ->
                    getEndDate = "" + eDay + "/" + (eMonth + 1) + "/" + eYear
                    if (getBeginDate != "")
                    {
                        if (frmt.parse(getEndDate)!! >= frmt.parse(getBeginDate))
                        {
                            activityToDoDetailsBinding.detailsToDoEndDate.setText(getEndDate)
                        }
                        else
                        {
                            Snackbar.make(v, "Tanggal berakhir tidak boleh sebelum tanggal mulai!", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Snackbar.make(v, "Mohon isi tanggal mulai terlebih dahulu!", Snackbar.LENGTH_SHORT).show()
                    }
                }, year, month, day)
                datePickerDialog.show()
            }

            R.id.details_to_do_save_button -> {
                validateToDoDetails(v)
            }
        }
    }

    private fun toDoDetails()
    {
        with(activityToDoDetailsBinding)
        {
            detailsToDoTitle.setText(getToDoData?.toDoTitle)
            detailsToDoBeginDate.setText(getToDoData?.beginDate)
            detailsToDoEndDate.setText(getToDoData?.endDate)
            detailsToDoDescription.setText(getToDoData?.description)
            detailsToDoAssignedTo.setText(getToDoData?.assignedTo)
            detailsToDoAssigneeGroup.setText(getToDoData?.assigneeGroup)
            detailsToDoProgress.setText(getToDoData?.progress.toString())
        }
    }

    private fun validateToDoDetails(v : View)
    {
        val titleInput = activityToDoDetailsBinding.detailsToDoTitle.text.toString()
        val beginDateInput = activityToDoDetailsBinding.detailsToDoBeginDate.text.toString()
        val endDateInput = activityToDoDetailsBinding.detailsToDoEndDate.text.toString()
        val descriptionInput = activityToDoDetailsBinding.detailsToDoDescription.text.toString()
        val assignedToInput = activityToDoDetailsBinding.detailsToDoAssignedTo.text.toString()
        val assigneeGroupInput = activityToDoDetailsBinding.detailsToDoAssigneeGroup.text.toString()
        val progressInput = activityToDoDetailsBinding.detailsToDoProgress.text.toString().toInt()
        val frmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)

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
        else if (progressInput > 100)
        {
            Snackbar.make(v, "Tingkat kemajuan tidak boleh lebih dari 100%!", Snackbar.LENGTH_SHORT).show()
        }
        else if (frmt.parse(endDateInput)!! < frmt.parse(beginDateInput))
        {
            Snackbar.make(v, "Tanggal berakhir tidak boleh sebelum tanggal mulai!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!getGroupName.contains(assigneeGroupInput))
        {
            Snackbar.make(v, "Pengguna bukan bagian dari grup tersebut!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            toDoData = ToDoData(getToDoData!!.toDoId, titleInput, beginDateInput, endDateInput, descriptionInput, assignedToInput, assigneeGroupInput, progressInput)
            saveChangedToDo(toDoData!!, v)
        }
    }

    private fun saveChangedToDo(toDoData : ToDoData, v : View)
    {
        if (RemoteDataSource().isOnline(this))
        {
            if (getToDoData!!.assigneeGroup != toDoData.assigneeGroup)
            {
                val groupIdBefore = getGroupId[getGroupName.indexOf(getToDoData!!.assigneeGroup)]
                val groupIdAfter = getGroupId[getGroupName.indexOf(toDoData.assigneeGroup)]
                toDoDetailsViewModel.setMoveToDoId(toDoData.toDoId, groupIdBefore, groupIdAfter)
            }

            val toDoChange = toDoData
            toDoDetailsViewModel.setToDoDetails(toDoChange)
            getToDoData = toDoChange
            Snackbar.make(v, "To do berhasil diperbaharui!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this, "Koneksi internet tidak aktif", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteToDo()
    {
        val assigneeGroupInput = activityToDoDetailsBinding.detailsToDoAssigneeGroup.text.toString()
        val deleteGroupId = getGroupId[getGroupName.indexOf(assigneeGroupInput)]
        toDoDetailsViewModel.setDeleteToDo(deleteGroupId, getToDoData!!.toDoId)
        Snackbar.make(view, "To do berhasil di hapus!", Snackbar.LENGTH_SHORT).show()
        Handler(mainLooper).postDelayed({
            finish()
        }, 1000)
    }
}