package com.example.collab.core

import androidx.lifecycle.LiveData
import com.example.collab.core.data.*

class DataSourceRepository private constructor(private val remoteDataSource : RemoteDataSource) {
    companion object
    {
        @Volatile
        private var instance : DataSourceRepository? = null

        fun getInstance(remoteDatasource : RemoteDataSource) : DataSourceRepository =
            instance ?: synchronized(this) {
                instance
                    ?: DataSourceRepository(remoteDatasource).apply {
                    instance = this
                }
            }
    }

    fun getLoginData(loginEmail : String) : LiveData<UserData>
    {
        return remoteDataSource.getLoginData(loginEmail)
    }

    fun getRegisterData(email : String) : LiveData<Boolean>
    {
        return remoteDataSource.getRegisterData(email)
    }

    fun getGroupListData(groupId : ArrayList<String>) : LiveData<List<GroupData>>
    {
        return remoteDataSource.getGroupListData(groupId)
    }

    fun getChannelListData(groupId : String) : LiveData<List<String>>
    {
        return remoteDataSource.getChannelListData(groupId)
    }

    fun getChannelNameData(channelId : ArrayList<String>) : LiveData<List<ChannelData>>
    {
        return remoteDataSource.getChannelNameData(channelId)
    }

    fun getMemberListData(groupId : String) : LiveData<List<MemberData>>
    {
        return remoteDataSource.getMemberListData(groupId)
    }

    fun getToDoListData(toDoIdList : ArrayList<String>) : LiveData<List<ToDoData>>
    {
        return remoteDataSource.getToDoListData(toDoIdList)
    }

    fun getSearchGroupData(groupName : String) : LiveData<Boolean>
    {
        return remoteDataSource.getSearchGroupData(groupName)
    }

    fun getJoinGroupData() : LiveData<GroupData>
    {
        return remoteDataSource.getJoinGroupData()
    }

    fun getGroupData(groupId : String, groupName : String) : LiveData<List<Boolean>>
    {
        return remoteDataSource.getGroupData(groupId, groupName)
    }

    fun getGroupMemberData(groupId : ArrayList<String>, memberId : String) : LiveData<List<MemberData>>
    {
        return remoteDataSource.getGroupMemberData(groupId, memberId)
    }

    fun getUserData(userId : String) : LiveData<UserData>
    {
        return remoteDataSource.getUserData(userId)
    }

    fun getSingleUserData(userId : String) : LiveData<UserData>
    {
        return remoteDataSource.getSingleUserData(userId)
    }

    fun getChatData(channelId : String) : LiveData<List<ChatData>>
    {
        return remoteDataSource.getChatData(channelId)
    }
}

// repository is used to do all data process