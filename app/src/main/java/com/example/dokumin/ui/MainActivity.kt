package com.example.dokumin.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.dokumin.R
import com.example.dokumin.databinding.ActivityMainBinding
import com.example.dokumin.ui.camerax.CameraXActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Handle navigation for fragments and CameraActivity
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_document -> {
                    navController.navigate(R.id.navigation_document)
                    true
                }
                R.id.navigation_camera -> {
                    // Start CameraActivity
                    val intent = Intent(this, CameraXActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_folder -> {
                    navController.navigate(R.id.navigation_folder)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
