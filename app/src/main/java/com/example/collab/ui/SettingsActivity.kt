package com.example.collab.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.collab.R
import com.example.collab.databinding.ActivitySettingsBinding
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activitySettingsBinding : ActivitySettingsBinding
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        setSupportActionBar(activitySettingsBinding.settingsToolbar)
        title = "Pengaturan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activitySettingsBinding.settingsLogoutButton.setOnClickListener(this)
        activitySettingsBinding.settingsNotificationSwitch.setOnClickListener(this)

        preferences = getSharedPreferences("application_setting", Context.MODE_PRIVATE)
        activitySettingsBinding.settingsNotificationSwitch.isChecked = preferences.getBoolean("notification", true)
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
            R.id.settings_notification_switch -> {
                saveNotificationSettings(v)
            }

            R.id.settings_logout_button -> {
                logoutSettings()
            }
        }
    }

    private fun saveNotificationSettings(v : View)
    {
        if (activitySettingsBinding.settingsNotificationSwitch.isChecked)
        {
            preferences.edit().putBoolean("notification", true).apply()
        }
        else
        {
            preferences.edit().putBoolean("notification", false).apply()
        }
        Snackbar.make(v, "Notifikasi berhasil diubah!", Snackbar.LENGTH_SHORT).show()
    }

    private fun logoutSettings()
    {
        val registerIntent = Intent(this@SettingsActivity, LoginActivity::class.java)
        startActivity(registerIntent)
        finish()
    }
}