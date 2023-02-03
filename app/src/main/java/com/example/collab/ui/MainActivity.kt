package com.example.collab.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.collab.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler()
    }

    private fun handler()
    {
        Handler(mainLooper).postDelayed({
            val splashIntent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, 1500)
    }
}