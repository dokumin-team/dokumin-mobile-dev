package com.example.dokumin.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dokumin.R
import com.example.dokumin.data.repositories.AuthRepository
import com.example.dokumin.data.repositories.AuthRepository.errorMessage
import com.example.dokumin.data.source.preferences.AppPreferences
import com.example.dokumin.data.source.remote.RetrofitConfig
import com.example.dokumin.databinding.ActivityMainBinding
import com.example.dokumin.databinding.ActivitySignUpBinding
import com.example.dokumin.ui.auth.otp.OtpActivity
import com.example.dokumin.ui.auth.signin.SignInActivity
import com.shashank.sony.fancytoastlib.FancyToast

class SignUpActivity : AppCompatActivity() {
    private var binding: ActivitySignUpBinding? = null
    private val appPreferences by lazy {
        AppPreferences(this@SignUpActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // validate the field
        binding?.apply {
            btnSignUp.setOnClickListener {

                val isValid = validateInput()
                if (!isValid) {
                    // show error
                    return@setOnClickListener
                }
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val name = etName.text.toString()

                // call the API
                AuthRepository.email = email
                AuthRepository.signUp(email = email, name = name, password = password)

            }
            loginTextBtn.setOnClickListener {
                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                startActivity(intent)
            }

        }

        observeSignUpResponse()
    }

    private fun observeSignUpResponse() {
        AuthRepository.signUpResponse.observe(this@SignUpActivity) {
            if (it != null) {
                // show success message
                FancyToast.makeText(
                    this,
                    it.message,
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,
                    true
                ).show();
                // retrieve token
                val token = it.token
                RetrofitConfig.token = token.toString()
                // save token to preferences
                appPreferences.saveSession(token.toString())
                // navigate to otp screen
                val intent = Intent(this, OtpActivity::class.java)
                startActivity(intent)
            }
        }

        errorMessage.observe(this) {
            if (it != null) {
                FancyToast.makeText(
                    this,
                    it,
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    true
                ).show();
            }
        }
    }

    private fun validateInput(): Boolean {
        binding?.apply {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val name = etName.text.toString()
            val confirmPassword = etRetypePassword.text.toString()
            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                return false
            }
            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                return false
            }
            if (name.isEmpty()) {
                etName.error = "Name is required"
                return false
            }
            if (confirmPassword.isEmpty()) {
                etRetypePassword.error = "Confirm password is required"
                return false
            }
            if (password != confirmPassword) {
                etRetypePassword.error = "Password does not match"
                return false
            }
        }
        return true

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}