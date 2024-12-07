package com.example.dokumin.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dokumin.R
import com.example.dokumin.data.repositories.AuthRepository
import com.example.dokumin.data.source.preferences.AppPreferences
import com.example.dokumin.data.source.remote.RetrofitConfig
import com.example.dokumin.databinding.ActivitySignInBinding
import com.example.dokumin.ui.MainActivity
import com.example.dokumin.ui.auth.otp.OtpActivity
import com.example.dokumin.ui.auth.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    private var binding : ActivitySignInBinding? = null
    private val appPreferences by lazy {
        AppPreferences(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.registerNowBtn?.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding?.signInBtn?.setOnClickListener {
            val formIsValid = validateInput()
            if (!formIsValid) {
                return@setOnClickListener
            }
            // call the API
            AuthRepository.signIn(
                binding?.etEmail?.text.toString(),
                binding?.etPassword?.text.toString()
            )

        }

        observeSignInResponse()

    }

    private fun observeSignInResponse() {
        AuthRepository.signInResponse.observe(this@SignInActivity) {
            if (it != null) {
                // show success message
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                // retrieve and save token
                val token = it.token
                RetrofitConfig.token = token.toString()
                // save to preferences
                appPreferences.saveSession(token.toString())

                if (appPreferences.getSession() != "") {
                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

//                val intent = Intent(this@SignInActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
            }
        }

        AuthRepository.errorMessage.observe(this) {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

                if (it == "Email has not been verified yet. Check your inbox!"){
                    val intent = Intent(this@SignInActivity, OtpActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    private fun validateInput(): Boolean {
        val email = binding?.etEmail?.text.toString()
        val password = binding?.etPassword?.text.toString()
        if (email.isEmpty()) {
            binding?.etEmail?.error = "Email is required"
            return false
        }
        if (password.isEmpty()) {
            binding?.etPassword?.error = "Password is required"
            return false
        }
        return true

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}