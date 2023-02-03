package com.example.collab.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.viewModel.LoginViewModel
import com.example.collab.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var activityLoginBinding : ActivityLoginBinding
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        activityLoginBinding.loginButton.setOnClickListener(this)
        activityLoginBinding.registerHereButton.setOnClickListener(this)

        val factory = ViewModelFactory.getInstance()
        // ViewModelProvider is used to create ViewModel and initializing
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.login_button -> {
                validateLogin(v)
            }

            R.id.register_here_button -> {
                val goToRegisterIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(goToRegisterIntent)
                finish()
            }
        }
    }

    private fun validateLogin(v : View)
    {
        val emailInput = activityLoginBinding.emailLogin.text.toString()
        val passwordInput = activityLoginBinding.passwordLogin.text.toString()

        if (emailInput.isEmpty() || passwordInput.isEmpty())
        {
            Snackbar.make(v, "Email atau password tidak boleh kosong!", Snackbar.LENGTH_SHORT).show()
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
        else
        {
            checkLogin(emailInput, passwordInput, v)
        }
    }

    private fun checkLogin(email : String, password : String, v : View)
    {
        loginViewModel.getLoginData(email).observe(this) {
            if (it.email != email)
            {
                Snackbar.make(v, "Email salah!", Snackbar.LENGTH_SHORT).show()
            }
            else if (it.password != password)
            {
                Snackbar.make(v, "Password salah!", Snackbar.LENGTH_SHORT).show()
            }
            else
            {
                Snackbar.make(v, "Login berhasil!", Snackbar.LENGTH_SHORT).show()
                Handler(mainLooper).postDelayed({
                    val loginIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                    loginIntent.putExtra("USER_DATA", it)
                    startActivity(loginIntent)
                    finish()
                }, 1000)
            }
        }
    }
}

/*regex for at least one with order [a-z]{1,}+[A-Z]{1,}+[0-9]{1,}

activity - viewmodel - repository - interface data source - class data source
         '-> ViewModelFactory - injection (put into viewmodel)

in module.app, android {} put this to use view binding as replacement for findViewById
buildFeatures {
    viewBinding = true
}
*/