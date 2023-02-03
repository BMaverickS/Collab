package com.example.collab.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.RegisterViewModel
import com.example.collab.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityRegisterBinding : ActivityRegisterBinding
    private lateinit var registerViewModel : RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(activityRegisterBinding.root)

        activityRegisterBinding.registerButton.setOnClickListener(this)
        activityRegisterBinding.loginHereButton.setOnClickListener(this)

        val factory = ViewModelFactory.getInstance()
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.register_button -> {
                validateRegister(v)
            }

            R.id.login_here_button -> {
                val goToLoginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(goToLoginIntent)
                finish()
            }
        }
    }

    private fun validateRegister(v : View)
    {
        val usernameInput = activityRegisterBinding.usernameRegister.text.toString()
        val emailInput = activityRegisterBinding.emailRegister.text.toString()
        val passwordInput = activityRegisterBinding.passwordRegister.text.toString()
        val confirmPasswordInput = activityRegisterBinding.confirmPasswordRegister.text.toString()

        if (usernameInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty())
        {
            Snackbar.make(v, "Mohon isi semua kolom yang tersedia!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!usernameInput.matches("[a-zA-Z0-9 ]+".toRegex()))
        {
            Snackbar.make(v, "Username hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (usernameInput.length < 5 || usernameInput.length > 25)
        {
            Snackbar.make(v, "Panjang username harus 5 - 25 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!emailInput.matches("[a-z0-9]+@+[a-z]+.+[a-z]".toRegex()))
        {
            Snackbar.make(v, "Format email salah!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!emailInput.endsWith(".com") && !emailInput.endsWith(".co.id"))
        {
            Snackbar.make(v, "Email harus berakhir dengan \".com\" atau \".co.id\"!", Snackbar.LENGTH_SHORT).show()
        }
        else if (!passwordInput.matches("[a-zA-Z0-9]+".toRegex()))
        {
            Snackbar.make(v, "Password hanya boleh terdiri dari huruf kecil, huruf kapital, dan angka!", Snackbar.LENGTH_SHORT).show()
        }
        else if (passwordInput.length < 4 || passwordInput.length > 20)
        {
            Snackbar.make(v, "Panjang password harus 4 - 20 karakter!", Snackbar.LENGTH_SHORT).show()
        }
        else if (confirmPasswordInput != passwordInput)
        {
            Snackbar.make(v, "Konfirmasi password tidak sama dengan password!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            checkRegister(emailInput, usernameInput, passwordInput, v)
        }
    }

    private fun checkRegister(email : String, username : String, password : String, v : View)
    {
        registerViewModel.getRegisterData(email).observe(this) {
            if (it == true)
            {
                Snackbar.make(v, "Email telah terdaftar!", Snackbar.LENGTH_SHORT).show()
            }
            else
            {
                saveRegister(email, username, password, v)
            }
        }
    }

    private fun saveRegister(email : String, username : String, password : String, v : View)
    {
        val regisData = UserData("", username, email, password, null)
        registerViewModel.setRegister(regisData)
        Snackbar.make(v, "Registrasi berhasil, kembali ke halaman Login!", Snackbar.LENGTH_SHORT).show()
        Handler(mainLooper).postDelayed({
            val registerIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(registerIntent)
            finish()
        }, 1000)
    }
}