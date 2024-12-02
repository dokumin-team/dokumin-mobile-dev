package com.example.dokumin.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.R
import com.example.dokumin.ui.MainActivity
import com.example.dokumin.ui.auth.signin.SignInActivity
import com.example.dokumin.ui.welcome.WelcomeActivity1
import com.example.dokumin.ui.welcome.WelcomeActivity2

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            checkFirstRun()
        }, 3000L)
    }

    private fun checkFirstRun() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("isFirstRun", false)
            editor.apply()

            val intent = Intent(this, WelcomeActivity1::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}
