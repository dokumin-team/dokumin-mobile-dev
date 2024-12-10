package com.example.dokumin.ui.document

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dokumin.R
import com.example.dokumin.data.model.responses.folder.Folder
import com.example.dokumin.data.repositories.FolderRepository
import com.example.dokumin.databinding.ActivityAddNewDocumentBinding
import com.shashank.sony.fancytoastlib.FancyToast

class AddNewDocumentActivity : AppCompatActivity() {
    private var binding: ActivityAddNewDocumentBinding? = null
    private var listAdapter: ArrayAdapter<Folder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewDocumentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        FolderRepository.getFolders()
        FolderRepository.selectedFolder = null

        listAdapter = ArrayAdapter(this, R.layout.item_dropdown_folder)
        binding?.autoComplete?.apply {
            setAdapter(listAdapter)
            onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val itemSelected = adapterView.getItemAtPosition(i)
                FolderRepository.selectedFolder = itemSelected as Folder?
            }
        }
        observeFolder()

        binding?.btnSelectDocument?.setOnClickListener {
            documentPickerLauncher.launch(arrayOf("*/*"))
        }

        binding?.btnSubmitDocument?.setOnClickListener {
            //check if folder is selected
            if (FolderRepository.selectedFolder == null) {
                FancyToast.makeText(
                    this@AddNewDocumentActivity,
                    "Please select a folder",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                )
                return@setOnClickListener
            }

            FolderRepository.postDocumentToFolder(
                context = this@AddNewDocumentActivity,
                uri = selectedDocumentUri ?: return@setOnClickListener,
            )
        }

        observeUploadDocument()
    }

    private var selectedDocumentUri: Uri? = null
    private var selectedDocumentName: String? = null

    private val documentPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedDocumentUri = uri
            selectedDocumentName = getFileName(uri)

            // Display selected document name
            binding?.tvSelectedDocumet?.text = selectedDocumentName ?: "No file selected"
        } else {
            binding?.tvSelectedDocumet?.text = "No file selected"
        }
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) it.getString(nameIndex) else null
            } else null
        }
    }


    private fun observeFolder() {
        FolderRepository.folderList.observe(this@AddNewDocumentActivity) { it ->
            listAdapter?.clear()
            listAdapter?.addAll(it?.folders ?: emptyList())
        }
    }

    private fun observeUploadDocument() {
        FolderRepository.postDocumentToFolderResponse.observe(this@AddNewDocumentActivity) { it ->
            if (it?.success == true) {
                FancyToast.makeText(
                    this@AddNewDocumentActivity,
                    "Document uploaded successfully",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    false
                )
                finish()
            } else {
                FancyToast.makeText(
                    this@AddNewDocumentActivity,
                    "Failed to upload document",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                )
            }
        }
    }
}