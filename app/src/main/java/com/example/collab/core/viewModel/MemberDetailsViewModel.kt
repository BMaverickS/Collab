package com.example.collab.core.viewModel

import androidx.lifecycle.ViewModel
import com.example.collab.core.data.RemoteDataSource

class MemberDetailsViewModel : ViewModel() {
    private val remoteDataSource = RemoteDataSource()

    fun setMemberStatus(memberId : String, memberStatus : String, groupId : String) =
        remoteDataSource.setMemberStatus(memberId, memberStatus, groupId)
    fun setRemoveMember(memberId : String, groupId : String) = remoteDataSource.setRemoveMember(memberId, groupId)
}