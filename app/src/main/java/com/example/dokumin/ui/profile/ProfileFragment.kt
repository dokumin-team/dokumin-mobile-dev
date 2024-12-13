package com.example.dokumin.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dokumin.R
import com.example.dokumin.data.source.preferences.AppPreferences
import com.example.dokumin.data.source.remote.RetrofitConfig
import com.example.dokumin.databinding.FragmentProfileBinding
import com.example.dokumin.ui.about.AboutActivity
import com.example.dokumin.ui.auth.signin.SignInActivity
import com.example.dokumin.ui.faq.FaqActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var binding: FragmentProfileBinding? = null
    private val appPreferences by lazy { AppPreferences(requireContext()) }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.userName?.text = appPreferences.getUserName()

        // Navigasi ke FAQ Activity
        binding?.FaqMenu?.setOnClickListener {
            val intent = Intent(requireContext(), FaqActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke About Activity
        binding?.AboutMenu?.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        // Logout dan kembali ke Login Activity
        binding?.logoutButton?.setOnClickListener {
            appPreferences.deleteSession()
            RetrofitConfig.token = ""
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}

