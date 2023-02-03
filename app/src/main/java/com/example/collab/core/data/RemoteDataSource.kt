package com.example.collab.core.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class RemoteDataSource {

    private lateinit var database : DatabaseReference
    private var groupNameData = ""
    private var i = 0

    fun isOnline(context : Context): Boolean
    {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null)
        {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            {
                return true
            }
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            {
                return true
            }
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            {
                return true
            }
        }
        return false
    }

    fun getLoginData(email : String) : LiveData<UserData>
    {
        val temp = MutableLiveData<UserData>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        val dbQuery = database.orderByChild("email").equalTo(email)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val tmp = ArrayList<UserData>()
                val grpId = ArrayList<String>()
                post.forEach {
                    val userId = it.child("userId").value.toString()
                    val username = it.child("username").value.toString()
                    val eml = it.child("email").value.toString()
                    val password = it.child("password").value.toString()
                    it.child("groupId").children.forEach {
                        grpId.add(it.value.toString())
                    }

                    val dt = UserData(userId, username, eml, password, grpId)
                    tmp.add(dt)
                }
                if (tmp.isEmpty())
                {
                    temp.postValue(UserData("", "", "", "", grpId))
                }
                else
                {
                    temp.postValue(tmp[0])
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Login Firebase", error.toException().toString())
            }
        }
        dbQuery.addValueEventListener(postListener)

        return temp
    }

    fun getRegisterData(email : String) : LiveData<Boolean>
    {
        val temp = MutableLiveData<Boolean>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")
        val dbQuery = database.orderByChild("email").equalTo(email)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    temp.postValue(true)
                }
                else
                {
                    temp.postValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Register Firebase", error.toException().toString())
            }
        }
        dbQuery.addListenerForSingleValueEvent(postListener)

        return temp
    }

    fun getGroupListData(groupIdList : ArrayList<String>) : LiveData<List<GroupData>>
    {
        val temp = MutableLiveData<List<GroupData>>()
        val tmp = ArrayList<GroupData>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        groupIdList.forEach {
            val dbQuery = database.orderByChild("groupId").equalTo(it)

            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.children

                    val chnId = ArrayList<String>()
                    val mbr = ArrayList<MemberData>()
                    val tdId = ArrayList<String>()
                    post.forEach {
                        val groupId = it.child("groupId").value.toString()
                        val groupName = it.child("groupName").value.toString()
                        val groupPassword = it.child("groupPassword").value.toString()
                        it.child("channelId").children.forEach {
                            chnId.add(it.value.toString())
                        }
                        it.child("member").children.forEach {
                            val mbrId = it.child("memberId").value.toString()
                            val mbrName = it.child("memberName").value.toString()
                            val mbrStatus = it.child("memberStatus").value.toString()
                            mbr.add(MemberData(mbrId, mbrName, mbrStatus))
                        }
                        it.child("toDoId").children.forEach {
                            tdId.add(it.value.toString())
                        }

                        val dt = GroupData(groupId, groupName, groupPassword, chnId, mbr, tdId)
                        if (tmp.isEmpty())
                        {
                            tmp.add(dt)
                        }
                        val i = tmp.indexOfFirst {
                            it.groupId == dt.groupId
                        }
                        if (i == -1)
                        {
                            tmp.add(dt)
                        }
                        else
                        {
                            tmp[i] = dt
                        }
                    }
                    if (groupIdList.isEmpty())
                    {
                        temp.postValue(listOf(GroupData("", "", "", chnId, mbr, tdId)))
                    }
                    else
                    {
                        temp.postValue(tmp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Cancel Group List Firebase", error.toException().toString())
                }
            }
            dbQuery.addValueEventListener(postListener)
        }
        if (groupIdList.isEmpty())
        {
            tmp.clear()
            temp.postValue(tmp)
            return temp
        }
        else
        {
            return temp
        }
    }

    fun getChannelListData(groupId : String) : LiveData<List<String>>
    {
        val temp = MutableLiveData<List<String>>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        val dbQuery = database.child(groupId).child("channelId")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val tmp = ArrayList<String>()
                post.forEach {
                    tmp.add(it.value.toString())
                }
                temp.postValue(tmp)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Channel List Firebase", error.toException().toString())
            }
        }
        dbQuery.addValueEventListener(postListener)

        return temp
    }

    fun getChannelNameData(channelIdList : ArrayList<String>) : LiveData<List<ChannelData>>
    {
        val temp = MutableLiveData<List<ChannelData>>()
        val tmp = ArrayList<ChannelData>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Channel")

        channelIdList.forEach {
            val dbQuery = database.orderByChild("channelId").equalTo(it)

            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.children

                    val cht = ArrayList<ChatData>()
                    var dt: ChannelData
                    post.forEach {
                        val channelId = it.child("channelId").value.toString()
                        val channelName = it.child("channelName").value.toString()

                        dt = ChannelData(channelId, channelName, cht)
                        if (tmp.isEmpty())
                        {
                            tmp.add(dt)
                        }
                        val i = tmp.indexOfFirst {
                            it.channelId == dt.channelId
                        }
                        if (i == -1)
                        {
                            tmp.add(dt)
                        }
                        else
                        {
                            tmp[i] = dt
                        }
                    }
                    temp.postValue(tmp)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Cancel Channel Name Firebase", error.toException().toString())
                }
            }
            dbQuery.addValueEventListener(postListener)
        }
        if (channelIdList.isEmpty())
        {
            tmp.clear()
            temp.postValue(tmp)
            return temp
        }
        else
        {
            return temp
        }
    }

    fun getMemberListData(groupId : String) : LiveData<List<MemberData>>
    {
        val temp = MutableLiveData<List<MemberData>>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        val dbQuery = database.orderByChild("groupId").equalTo(groupId)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val mbr = ArrayList<MemberData>()
                post.forEach {
                    it.child("member").children.forEach {
                        val mbrId = it.child("memberId").value.toString()
                        val mbrName = it.child("memberName").value.toString()
                        val mbrStatus = it.child("memberStatus").value.toString()
                        mbr.add(MemberData(mbrId, mbrName, mbrStatus))
                    }
                }
                temp.postValue(mbr)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Member List Firebase", error.toException().toString())
            }
        }
        dbQuery.addValueEventListener(postListener)

        return temp
    }

    fun getToDoListData(toDoIdList : ArrayList<String>) : LiveData<List<ToDoData>>
    {
        val temp = MutableLiveData<List<ToDoData>>()
        val tmp = ArrayList<ToDoData>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("ToDo")

        toDoIdList.forEach {
            val dbQuery = database.orderByChild("toDoId").equalTo(it)

            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.children

                    post.forEach {
                        val toDoId = it.child("toDoId").value.toString()
                        val toDoTitle = it.child("toDoTitle").value.toString()
                        val beginDate = it.child("beginDate").value.toString()
                        val endDate = it.child("endDate").value.toString()
                        val description  = it.child("description").value.toString()
                        val assignedTo  = it.child("assignedTo").value.toString()
                        val assigneeGroup = it.child("assigneeGroup").value.toString()
                        val progress = it.child("progress").value.toString().toInt()

                        val dt = ToDoData(toDoId, toDoTitle, beginDate, endDate, description, assignedTo, assigneeGroup, progress)
                        if (tmp.isEmpty())
                        {
                            tmp.add(dt)
                        }
                        val i = tmp.indexOfFirst {
                            it.toDoId == dt.toDoId
                        }
                        if (i == -1)
                        {
                            tmp.add(dt)
                        }
                        else
                        {
                            tmp[i] = dt
                        }
                    }
                    if (tmp.isEmpty())
                    {
                        temp.postValue(ArrayList())
                    }
                    else
                    {
                        temp.postValue(tmp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Cancel To Do List Firebase", error.toException().toString())
                }
            }
            dbQuery.addValueEventListener(postListener)
        }

        return temp
    }

    fun getSearchGroupData(groupName : String) : LiveData<Boolean>
    {
        val temp = MutableLiveData<Boolean>()
        groupNameData = groupName

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        val dbQuery = database.orderByChild("groupName").equalTo(groupName)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    temp.postValue(true)
                }
                else
                {
                    temp.postValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Search Group Firebase", error.toException().toString())
            }
        }
        dbQuery.addListenerForSingleValueEvent(postListener)

        return temp
    }

    fun getJoinGroupData() : LiveData<GroupData>
    {
        val temp = MutableLiveData<GroupData>()
        var tmp : GroupData

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        val dbQuery = database.orderByChild("groupName").equalTo(groupNameData)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val chnId = ArrayList<String>()
                val mbr = ArrayList<MemberData>()
                val tdId = ArrayList<String>()
                post.forEach {
                    val groupId = it.child("groupId").value.toString()
                    val groupName = it.child("groupName").value.toString()
                    val groupPassword = it.child("groupPassword").value.toString()

                    tmp = GroupData(groupId, groupName, groupPassword, chnId, mbr, tdId)
                    temp.postValue(tmp)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Join Group Firebase", error.toException().toString())
            }
        }
        dbQuery.addListenerForSingleValueEvent(postListener)

        return temp
    }

    fun getGroupData(groupName : String, groupId : String) : LiveData<List<Boolean>>
    {
        val temp = MutableLiveData<List<Boolean>>()
        val res = ArrayList<Boolean>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")
        val dbQuery = database.orderByChild("groupName").equalTo(groupName)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    res.add(true)
                    temp.postValue(res)
                }
                else
                {
                    res.add(false)
                    temp.postValue(res)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Group Name Firebase", error.toException().toString())
            }
        }
        dbQuery.addValueEventListener(postListener)

        val query = database.orderByChild("groupId").equalTo(groupId)

        val pListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    res.add(true)
                    temp.postValue(res)
                }
                else
                {
                    res.add(false)
                    temp.postValue(res)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Group Id Firebase", error.toException().toString())
            }
        }
        query.addListenerForSingleValueEvent(pListener)

        return temp
    }

    fun getGroupMemberData(groupId : ArrayList<String>, memberId : String) : LiveData<List<MemberData>>
    {
        val temp = MutableLiveData<List<MemberData>>()
        val tmp = ArrayList<MemberData>()
        val id = ArrayList<String>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        groupId.forEach {
            val dbQuery = database.orderByChild("groupId").equalTo(it)
            val grId = it

            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.children

                    post.forEach {
                        it.child("member").children.forEach {
                            val mbrId = it.child("memberId").value.toString()
                            val mbrName = it.child("memberName").value.toString()
                            val mbrStatus = it.child("memberStatus").value.toString()
                            val mbr : MemberData
                            if (mbrStatus == "Pemimpin tim" && mbrId == memberId)
                            {
                                mbr = MemberData(grId, mbrName, mbrStatus)
                                tmp.add(mbr)
                                id.add(grId)
                            }
                        }
                        temp.postValue(tmp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Cancel Group Member Firebase", error.toException().toString())
                }
            }
            dbQuery.addListenerForSingleValueEvent(postListener)
        }

        return temp
    }

    fun getUserData(userId : String) : LiveData<UserData>
    {
        val temp = MutableLiveData<UserData>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        val dbQuery = database.orderByChild("userId").equalTo(userId)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val tmp = ArrayList<UserData>()
                val grpId = ArrayList<String>()
                post.forEach {
                    val usersId = it.child("userId").value.toString()
                    val username = it.child("username").value.toString()
                    val email = it.child("email").value.toString()
                    val password = it.child("password").value.toString()
                    it.child("groupId").children.forEach {
                        grpId.add(it.value.toString())
                    }
                    val dt = UserData(usersId, username, email, password, grpId)
                    tmp.add(dt)
                }
                if (tmp.isEmpty())
                {
                    temp.postValue(UserData("", "", "", "", grpId))
                }
                else
                {
                    temp.postValue(tmp[0])
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel User Firebase", error.toException().toString())
            }
        }
        dbQuery.addValueEventListener(postListener)

        return temp
    }

    fun getSingleUserData(userId : String) : LiveData<UserData>
    {
        val temp = MutableLiveData<UserData>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        val dbQuery = database.orderByChild("userId").equalTo(userId)

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val tmp = ArrayList<UserData>()
                val grpId = ArrayList<String>()
                post.forEach {
                    val usersId = it.child("userId").value.toString()
                    val username = it.child("username").value.toString()
                    val email = it.child("email").value.toString()
                    val password = it.child("password").value.toString()
                    it.child("groupId").children.forEach {
                        grpId.add(it.value.toString())
                    }
                    val dt = UserData(usersId, username, email, password, grpId)
                    tmp.add(dt)
                }
                if (tmp.isEmpty())
                {
                    temp.postValue(UserData("", "", "", "", grpId))
                }
                else
                {
                    temp.postValue(tmp[0])
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel User Firebase", error.toException().toString())
            }
        }
        dbQuery.addListenerForSingleValueEvent(postListener)

        return temp
    }

    fun getChatData(channelId : String) : LiveData<List<ChatData>>
    {
        val temp = MutableLiveData<List<ChatData>>()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Channel")

        val dbQuery = database.child(channelId).child("chat")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val cht = ArrayList<ChatData>()
                post.forEach {
                    val message = it.child("message").value.toString()
                    val messageSender = it.child("messageSender").value.toString()

                    cht.add(ChatData(messageSender, message))
                }
                temp.postValue(cht)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Chat Firebase", error.toException().toString())
            }
        }
        dbQuery.addValueEventListener(postListener)

        return temp
    }

    fun setRegister(userData : UserData)
    {
        var countNumber : Int

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                var userId = ""
                val username = userData.username
                val email = userData.email
                val password = userData.password
                val strTemp = post.last().child("userId").value.toString()
                countNumber = strTemp.substring(2).toInt() + 1
                if ((countNumber) < 10)
                {
                    userId = "US000${countNumber}"
                }
                else if ((countNumber) < 100)
                {
                    userId = "US00${countNumber}"
                }
                else if ((countNumber) < 1000)
                {
                    userId = "US0${countNumber}"
                }
                else if ((countNumber) > 999)
                {
                    userId = "US${countNumber}"
                }
                val user = UserData(userId, username, email, password, null)

                database.child(userId).setValue(user).addOnFailureListener {
                    Log.d("Cancel Set Value Register Firebase", it.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Set Register Firebase", error.toException().toString())
            }
        }
        database.addListenerForSingleValueEvent(postListener)
    }

    fun setSaveJoinGroup(userData : UserData, groupId : String)
    {
        val temp = MemberData(userData.userId, userData.username, "Anggota tim")

        // save group id to user
        val db = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        db.child(userData.userId).child("groupId").child(groupId).setValue(groupId).addOnFailureListener {
            Log.d("Cancel Set Value Join Group (User) Firebase", it.toString())
        }
        //save new joined group
        val dtbase = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        dtbase.child(groupId).child("member").child(userData.userId).setValue(temp).addOnFailureListener {
            Log.d("Cancel Set Value Join Group (Group) Firebase", it.toString())
        }
    }

    fun setCreateGroup(groupData : GroupData)
    {
        val temp = GroupData(groupData.groupId, groupData.groupName, groupData.groupPassword, null, null, null)
        val tmp = groupData.member?.get(0)?.memberId.toString()

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        database.child(groupData.groupId).setValue(temp).addOnFailureListener {
            Log.d("Cancel Set Value Create Group (Group) Firebase", it.toString())
        }

        database.child(groupData.groupId).child("member").child(tmp).setValue(groupData.member?.get(0)).addOnFailureListener {
            Log.d("Cancel Set Value Create Group (Member) Firebase", it.toString())
        }

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        database.child(tmp).child("groupId").child(groupData.groupId).setValue(groupData.groupId).addOnFailureListener {
            Log.d("Cancel Set Value Create Group (User) Firebase", it.toString())
        }
    }

    fun setCreateToDo(toDoData : ToDoData, saveGroupId : String)
    {
        var countNumber : Int
        var toDoId = ""

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("ToDo")
        val db = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val toDoTitle = toDoData.toDoTitle
                val beginDate = toDoData.beginDate
                val endDate = toDoData.endDate
                val description = toDoData.description
                val assignedTo = toDoData.assignedTo
                val assigneeGroup = toDoData.assigneeGroup
                val progress = toDoData.progress
                val strTemp = post.last().child("toDoId").value.toString()

                countNumber = strTemp.substring(2).toInt() + 1
                if ((countNumber) < 10)
                {
                    toDoId = "TO000${countNumber}"
                }
                else if ((countNumber) < 100)
                {
                    toDoId = "TO00${countNumber}"
                }
                else if ((countNumber) < 1000)
                {
                    toDoId = "TO0${countNumber}"
                }
                else if ((countNumber) > 999)
                {
                    toDoId = "TO${countNumber}"
                }
                val toDo = ToDoData(toDoId, toDoTitle, beginDate, endDate, description, assignedTo, assigneeGroup, progress)

                if (i == 0)
                {
                    database.child(toDoId).setValue(toDo).addOnFailureListener {
                        Log.d("Cancel Set Value Create To Do (To Do) Firebase", it.toString())
                    }

                    //add to do to group
                    db.child(saveGroupId).child("toDoId").child(toDoId).setValue(toDoId).addOnFailureListener {
                        Log.d("Cancel Set Value Create To Do (Group) Firebase", it.toString())
                    }
                    i++
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Set Create To Do Firebase", error.toException().toString())
            }
        }
        database.addListenerForSingleValueEvent(postListener)
    }

    fun setToDoDetails(toDoData : ToDoData)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("ToDo")

        database.child(toDoData.toDoId).setValue(toDoData).addOnFailureListener {
            Log.d("Cancel Set Value To Do Details Firebase", it.toString())
        }
    }

    fun setMoveToDoId(toDoId : String, groupIdBefore : String, groupIdAfter : String)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        database.child(groupIdAfter).child("toDoId").child(toDoId).setValue(toDoId).addOnFailureListener {
            Log.d("Cancel Set Value To Do Details Firebase", it.toString())
        }

        database.child(groupIdBefore).child("toDoId").child(toDoId).removeValue().addOnFailureListener {
            Log.d("Cancel Set Value To Do Details Firebase", it.toString())
        }
    }

    fun setProfile(userData : UserData)
    {
        val usData = UserData(userData.userId, userData.username, userData.email, userData.password, null)
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        database.child(userData.userId).setValue(usData).addOnFailureListener {
            Log.d("Cancel Set Value Profile (User) Firebase", it.toString())
        }

        userData.groupId?.forEach {
            database.child(userData.userId).child("groupId").child(it).setValue(it).addOnFailureListener {
                Log.d("Cancel Set Value Profile (User) Firebase", it.toString())
            }
        }

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        userData.groupId?.forEach {
            database.child(it).child("member").child(userData.userId).child("memberName").setValue(userData.username).addOnFailureListener {
                Log.d("Cancel Set Value Profile (Group) Firebase", it.toString())
            }
        }
    }

    fun setChannel(groupId : String, channelName : String)
    {
        var db : DatabaseReference
        var countNumber : Int
        var channelId = ""

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Channel")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val strTemp = post.last().child("channelId").value.toString()
                countNumber = strTemp.substring(2).toInt() + 1
                if ((countNumber) < 10)
                {
                    channelId = "CH000${countNumber}"
                }
                else if ((countNumber) < 100)
                {
                    channelId = "CH00${countNumber}"
                }
                else if ((countNumber) < 1000)
                {
                    channelId = "CH0${countNumber}"
                }
                else if ((countNumber) > 999)
                {
                    channelId = "CH${countNumber}"
                }
                val channel = ChannelData(channelId, channelName, ArrayList())
                database.child(channelId).setValue(channel).addOnFailureListener {
                    Log.d("Cancel Set Value Channel (Channel) Firebase", it.toString())
                }

                db = FirebaseDatabase
                    .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Group")

                db.child(groupId).child("channelId").child(channelId).setValue(channelId).addOnFailureListener {
                    Log.d("Cancel Set Value Channel (Group) Firebase", it.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Set Channel Firebase", error.toException().toString())
            }
        }
        database.addListenerForSingleValueEvent(postListener)
    }

    fun setMemberStatus(memberId : String, memberStatus : String, groupId : String)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        database.child(groupId).child("member").child(memberId).child("memberStatus").setValue(memberStatus).addOnFailureListener {
            Log.d("Cancel Set Value Member Status Firebase", it.toString())
        }
    }

    fun setRemoveMember(memberId : String, groupId : String)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        database.child(groupId).child("member").child(memberId).removeValue().addOnFailureListener {
            Log.d("Cancel Remove Value Member (Group) Firebase", it.toString())
        }

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        database.child(memberId).child("groupId").child(groupId).removeValue().addOnFailureListener {
            Log.d("Cancel Remove Value Member (User) Firebase", it.toString())
        }
    }

    fun setChatMessage(channelId : String, chatData : ChatData)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Channel")

        val dbQuery = database.child(channelId).child("chat")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val key = post.count().toString()

                database.child(channelId).child("chat").child(key).setValue(chatData).addOnFailureListener {
                    Log.d("Cancel Set Value Chat Firebase", it.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Set Chat Firebase", error.toException().toString())
            }
        }
        dbQuery.addListenerForSingleValueEvent(postListener)
    }

    fun setDeleteToDo(groupId: String, toDoId: String)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        database.child(groupId).child("toDoId").child(toDoId).removeValue().addOnFailureListener {
            Log.d("Cancel Remove Value To Do (Group) Firebase", it.toString())
        }

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("ToDo")

        database.child(toDoId).removeValue().addOnFailureListener {
            Log.d("Cancel Remove Value To Do (To Do) Firebase", it.toString())
        }
    }

    fun setDeleteGroup(groupId : String, userId : String)
    {
        val channelId = ArrayList<String>()
        val toDoId = ArrayList<String>()

        val databaseGroup = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group").child(groupId)
        val databaseChannel = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Channel")
        val databaseToDo = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("ToDo")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("channelId").children.forEach {
                    channelId.add(it.value.toString())
                    databaseChannel.child(it.value.toString()).removeValue().addOnFailureListener {
                        Log.d("Cancel Remove Value Group (Channel) Firebase", it.toString())
                    }
                }
                snapshot.child("toDoId").children.forEach {
                    toDoId.add(it.value.toString())
                    databaseToDo.child(it.value.toString()).removeValue().addOnFailureListener {
                        Log.d("Cancel Remove Value Group (To Do) Firebase", it.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Remove Group Firebase", error.toException().toString())
            }
        }
        databaseGroup.addListenerForSingleValueEvent(postListener)

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")

        database.child(groupId).removeValue().addOnFailureListener {
            Log.d("Cancel Remove Value Group (Group) Firebase", it.toString())
        }

        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        database.child(userId).child("groupId").child(groupId).removeValue().addOnFailureListener {
            Log.d("Cancel Remove Value Group (User) Firebase", it.toString())
        }
    }

    fun setDeleteChannel(groupId : String)
    {
        database = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Group")
        val db = FirebaseDatabase
            .getInstance("https://collab-37c27-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Channel")

        val dbQuery = database.child(groupId).child("channelId")

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.children

                val tmp = ArrayList<String>()
                post.forEach {
                    tmp.add(it.value.toString())
                }
                tmp.forEach {
                    database.child(groupId).child("channelId").child(it).removeValue().addOnFailureListener {
                        Log.d("Cancel Remove Value Channel (Group) Firebase", it.toString())
                    }

                    db.child(it).removeValue().addOnFailureListener {
                        Log.d("Cancel Remove Value Channel (Channel) Firebase", it.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Cancel Channel List Firebase", error.toException().toString())
            }
        }
        dbQuery.addListenerForSingleValueEvent(postListener)
    }
}

// children -> array / object that needs iteration
// child -> object
