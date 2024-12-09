package com.example.dokumin.ui.folder.detailfolder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dokumin.adapter.DocumentAdapter
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.data.repositories.FolderRepository
import com.example.dokumin.databinding.ActivityDetailFolderBinding
import com.shashank.sony.fancytoastlib.FancyToast

class DetailFolderActivity : AppCompatActivity() {
    private var binding: ActivityDetailFolderBinding? = null
    private var documentAdapter: DocumentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailFolderBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        FolderRepository.selectedFolder?.id.let {
            DocumentRepository.getDocumentsByFolder(it)
        }
        binding?.tvDokumen?.text = FolderRepository.selectedFolder?.folderName
        setupRvDocument()
        observeListDocumentFolder()
    }

    private fun observeListDocumentFolder() {
        DocumentRepository.documentFolderList.observe(this) {
            documentAdapter?.setList(it)
        }
        DocumentRepository.errorMessage.observe(this) {
            it?.let {
                FancyToast.makeText(
                    this,
                    it,
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }

    }

    private fun setupRvDocument() {
        documentAdapter = DocumentAdapter(
            onDocument = ::onDocumentClick
        )

        binding?.rvDocument?.adapter = documentAdapter
        binding?.rvDocument?.layoutManager =
            LinearLayoutManager(this@DetailFolderActivity, LinearLayoutManager.VERTICAL, false)

    }

    private fun onDocumentClick(doc: Document?) {
        DocumentRepository.selectedDocument = doc
        when {
            doc?.fileType!!.contains("application/pdf") -> {
                DocumentRepository.selectedDocType = DocType.PDF
            }

            doc?.fileType!!.contains("application/doc") -> {
                DocumentRepository.selectedDocType = DocType.DOC
            }

            doc?.fileType!!.contains("text/plain") -> {
                DocumentRepository.selectedDocType = DocType.TXT
            }

            doc?.fileType!!.contains("image/") -> {
                DocumentRepository.selectedDocType = DocType.IMAGE
            }

            else -> {
                FancyToast.makeText(
                    this@DetailFolderActivity,
                    "File type not supported",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
                DocumentRepository.selectedDocType = DocType.UNKNOWN
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}