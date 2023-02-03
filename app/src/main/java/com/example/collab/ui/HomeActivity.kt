package com.example.collab.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel : HomeViewModel
    private var toDoId = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        val factory = ViewModelFactory.getInstance()
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        val getGroupId = intent.getParcelableExtra<UserData>("USER_DATA")?.groupId
        if (getGroupId != null)
        {
            checkNotification(getGroupId)
        }
    }
    
    private fun checkNotification(groupId : ArrayList<String>)
    {
        val preferences = getSharedPreferences("application_setting", Context.MODE_PRIVATE)
        if (preferences.getBoolean("notification", true))
        {
            homeViewModel.getGroupListData(groupId).observe(this) {
                it.forEach {
                    it.toDoId?.forEach {
                        toDoId.add(it)
                    }
                }
                homeViewModel.getToDoListData(toDoId).observe(this) {
                    val checkDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Calendar.getInstance().time)
                    val frmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    it.forEach {
                        if (frmt.parse(it.endDate) < frmt.parse(checkDate) && it.progress < 100)
                        {
                            sendNotification()
                        }
                    }
                }
            }
        }
    }

    private fun sendNotification()
    {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, "NOTIF_TO_DO")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Pengingat")
            .setContentText("Mohon segera menyelesaikan to do yang masih ada!")
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel("NOTIF_TO_DO", "TO_DO_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "TO_DO_CHANNEL"
            builder.setChannelId("NOTIF_TO_DO")
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManager.notify(1, notification)
    }
}