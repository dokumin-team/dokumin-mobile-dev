package com.example.dokumin.ui.folder.addFolder

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.dokumin.data.repositories.FolderRepository
import com.example.dokumin.databinding.ActivityAddFolderBinding
import com.shashank.sony.fancytoastlib.FancyToast

class AddFolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveFolder.setOnClickListener {
            val folderName = binding.etFolderName.text.toString()
            if (folderName.isBlank()) {
                FancyToast.makeText(
                    this@AddFolderActivity,
                    "empty field",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
            } else {
                postFolder(folderName)
            }
        }

        observeFolderCreation()
    }

    private fun postFolder(folderName: String) {
        FolderRepository.postFolder(folderName)
    }

    private fun observeFolderCreation() {
        FolderRepository.createFolderResponse.observe(this, Observer { response ->
            if (response != null) {
                FancyToast.makeText(
                    this@AddFolderActivity,
                    "create folder successfully",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    false
                ).show()
                finish()
            }
        })

        FolderRepository.errorMessage.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                FancyToast.makeText(
                    this@AddFolderActivity,
                    "failed to create folder",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })
    }
}
