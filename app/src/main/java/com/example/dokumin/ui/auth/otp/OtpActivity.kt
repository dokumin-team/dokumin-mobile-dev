package com.example.dokumin.ui.auth.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dokumin.R
import com.example.dokumin.data.repositories.AuthRepository
import com.example.dokumin.databinding.ActivityOtpBinding
import com.example.dokumin.ui.MainActivity
import com.example.dokumin.ui.auth.signin.SignInActivity

class OtpActivity : AppCompatActivity() {
    private var binding: ActivityOtpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding?.root)

        startOtpTimer()
        binding?.otpVerivy?.setOnClickListener {
            val otp =
                binding?.etOtp1?.text.toString()
                    .plus(binding?.etOtp2?.text.toString())
                    .plus(binding?.etOtp3?.text.toString())
                    .plus(binding?.etOtp4?.text.toString())

            AuthRepository.verifyOtp(otp)
        }

        observeOtpResponse()
    }


    private fun startOtpTimer() {

        val timerDuration = 60_000L
        val interval = 1_000L

        val countDownTimer = object : CountDownTimer(timerDuration, interval) {
            override fun onTick(millisUntilFinished: Long) {

                binding?.resendCode?.text = "Resend code in ${millisUntilFinished / 1000}s"

                binding?.resendCode?.isEnabled = false
            }

            override fun onFinish() {

                binding?.resendCode?.text = "Resend code"
                binding?.resendCode?.isEnabled = true

                binding?.resendCode?.setOnClickListener {
                    AuthRepository.resendOtp(AuthRepository.email)

                    startOtpTimer()
                }
            }
        }

        // Start the timer
        countDownTimer.start()
    }

    private fun observeOtpResponse() {
        AuthRepository.resendOtpResponse.observe(this@OtpActivity) {
            if (it != null) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        AuthRepository.verifyOtpResponse.observe(this@OtpActivity) {
            if (it != null) {
                Toast.makeText(this, "Verifikasi OTP Berhasil, Silahkan Login", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this@OtpActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}