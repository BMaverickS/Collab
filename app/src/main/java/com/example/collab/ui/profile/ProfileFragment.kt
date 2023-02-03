package com.example.collab.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collab.R
import com.example.collab.core.ViewModelFactory
import com.example.collab.core.data.RemoteDataSource
import com.example.collab.core.data.UserData
import com.example.collab.core.viewModel.ProfileViewModel
import com.example.collab.databinding.FragmentProfileBinding
import com.example.collab.ui.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentProfileBinding : FragmentProfileBinding
    private lateinit var profileViewModel : ProfileViewModel
    private var getProfile : UserData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentProfileBinding.settingsImage.setOnClickListener(this)
        fragmentProfileBinding.saveProfileButton.setOnClickListener(this)

        getProfile = activity?.intent?.getParcelableExtra("USER_DATA")

        val factory = ViewModelFactory.getInstance()
        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        getProfile()
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.settings_image -> {
                val testIntent = Intent(activity, SettingsActivity::class.java)
                startActivity(testIntent)
            }

            R.id.save_profile_button -> {
                validateProfile(v)
            }
        }
    }

    private fun getProfile()
    {
        if (RemoteDataSource().isOnline(requireContext()))
        {
            profileViewModel.getUserData(getProfile?.userId!!).observe(viewLifecycleOwner) {
                with (fragmentProfileBinding)
                {
                    userProfileId.setText(it.userId)
                    userProfileUsername.setText(it.username)
                    userProfileEmail.setText(it.email)
                }
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Koneksi internet tidak aktif", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateProfile(v : View)
    {
        val usernameInput = fragmentProfileBinding.userProfileUsername.text.toString()
        val emailInput = fragmentProfileBinding.userProfileEmail.text.toString()
        val passwordInput = fragmentProfileBinding.userProfilePassword.text.toString()
        val confirmPasswordInput = fragmentProfileBinding.userProfileConfirmPassword.text.toString()

        if (usernameInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty())
        {
            Snackbar.make(v, "Mohon isi semua kolom pengisian data!", Snackbar.LENGTH_SHORT).show()
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
            checkProfile(usernameInput, emailInput, passwordInput, v)
        }
    }

    private fun checkProfile(username : String, email : String, password : String, v : View)
    {
        profileViewModel.getUserData(getProfile?.userId!!).observe(viewLifecycleOwner) {
            val emailProfile = it.email
            getProfile!!.groupId = it.groupId
            profileViewModel.getProfileData(email).observe(this) {
                if (it == true && email != emailProfile)
                {
                    Snackbar.make(v, "Email telah terdaftar!", Snackbar.LENGTH_SHORT).show()
                }
                else
                {
                    saveProfile(username, email, password, v)
                }
            }
        }
    }

    private fun saveProfile(username : String, email : String, password : String, v : View)
    {
        if (RemoteDataSource().isOnline(requireContext()))
        {
            val profileData = UserData(getProfile?.userId!!, username, email, password, getProfile?.groupId!!)
            profileViewModel.setProfile(profileData)
            Snackbar.make(v, "Data profil telah berhasil disimpan!", Snackbar.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(requireContext(), "Koneksi internet tidak aktif", Toast.LENGTH_SHORT).show()
        }
    }
}