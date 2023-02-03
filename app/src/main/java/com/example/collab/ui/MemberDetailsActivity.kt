package com.example.collab.ui

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.data.MemberData
import com.example.collab.core.viewModel.MemberDetailsViewModel
import com.example.collab.databinding.ActivityMemberDetailsBinding
import com.google.android.material.snackbar.Snackbar

class MemberDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityMemberDetailsBinding : ActivityMemberDetailsBinding
    private lateinit var memberDetailsViewModel : MemberDetailsViewModel
    private var getMemberDetails : MemberData? = null
    private var memberStatus = ""
    private var memberGroupId = ""

    private var memberStatusList = arrayOf("Pemimpin tim", "Anggota tim")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMemberDetailsBinding = ActivityMemberDetailsBinding.inflate(layoutInflater)
        setContentView(activityMemberDetailsBinding.root)

        setSupportActionBar(activityMemberDetailsBinding.memberDetailsToolbar)
        title = "Detil Member"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        memberDetailsViewModel = ViewModelProvider(this).get(MemberDetailsViewModel::class.java)

        getMemberDetails = intent.getParcelableExtra("MEMBER_DETAILS_DATA")
        memberGroupId = intent.getStringExtra("MEMBER_GROUP_ID")!!
        memberDetails()

        activityMemberDetailsBinding.memberDetailsSave.setOnClickListener(this)
        activityMemberDetailsBinding.memberDetailsRemove.setOnClickListener(this)
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
        when (v?.id)
        {
            R.id.member_details_save -> {
                saveMemberStatus(v)
            }

            R.id.member_details_remove -> {
                removeMember(v)
            }
        }
    }

    private fun memberDetails()
    {
        activityMemberDetailsBinding.memberDetailsUsername.setText(getMemberDetails?.memberName)

        val adapter = ArrayAdapter(this, R.layout.spinner_item, memberStatusList)
        activityMemberDetailsBinding.memberDetailsStatus.adapter = adapter
        activityMemberDetailsBinding.memberDetailsStatus.setSelection(adapter.getPosition(getMemberDetails?.memberStatus))
        memberStatus = getMemberDetails!!.memberStatus

        activityMemberDetailsBinding.memberDetailsStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                memberStatus = memberStatusList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun saveMemberStatus(v : View)
    {
        memberDetailsViewModel.setMemberStatus(getMemberDetails!!.memberId, memberStatus, memberGroupId)
        Snackbar.make(v, "Status anggota berhasil diubah!", Snackbar.LENGTH_SHORT).show()
    }

    private fun removeMember(v : View)
    {
        val totalMember = intent.getIntExtra("TOTAL_MEMBER", 0)
        if (totalMember == 1)
        {
            Snackbar.make(v, "Anggota tidak bisa dihapus!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            memberDetailsViewModel.setRemoveMember(getMemberDetails!!.memberId, memberGroupId)
            Snackbar.make(v, "Anggota berhasil dihapus!", Snackbar.LENGTH_SHORT).show()
            Handler(mainLooper).postDelayed({
                finish()
            }, 1000)
        }
    }
}