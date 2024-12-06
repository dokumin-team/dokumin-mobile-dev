package com.example.dokumin.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.dokumin.R
import android.content.Intent
import com.example.dokumin.ui.about.AboutActivity
import com.example.dokumin.ui.auth.signin.SignInActivity
import com.example.dokumin.ui.faq.FaqActivity
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigasi ke FAQ Activity
        view.findViewById<View>(R.id.FaqMenu).setOnClickListener {
            val intent = Intent(requireContext(), FaqActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke About Activity
        view.findViewById<View>(R.id.AboutMenu).setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        // Logout dan kembali ke Login Activity
        view.findViewById<MaterialButton>(R.id.logoutButton).setOnClickListener {
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}

