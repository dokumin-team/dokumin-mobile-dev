package com.example.dokumin.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.R
import com.example.dokumin.data.source.preferences.AppPreferences
import com.example.dokumin.data.source.remote.RetrofitConfig
import com.example.dokumin.ui.MainActivity
import com.example.dokumin.ui.auth.signin.SignInActivity
import com.example.dokumin.ui.welcome.WelcomeActivity1

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val appPreferences by lazy { AppPreferences(this) }
    private val userToken by lazy { appPreferences.getSession() }
    private val isFirstRun by lazy { appPreferences.isFirstRun() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        RetrofitConfig.token = userToken;
//        Handler(Looper.getMainLooper()).postDelayed({
        checkFirstRun()
//        }, 3000L)
    }

    private fun checkFirstRun() {
        if (isFirstRun) {
            appPreferences.updateFirstRun()
            val intent = Intent(this, WelcomeActivity1::class.java)
            startActivity(intent)
        } else {
            if (RetrofitConfig.token != "") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }

        }

        finish()
    }
}
