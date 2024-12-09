package com.example.dokumin.ui.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dokumin.adapter.DocumentAdapter
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.data.repositories.DocumentRepository.countDocument
import com.example.dokumin.data.repositories.DocumentRepository.newestDocumentList
import com.example.dokumin.data.repositories.FolderRepository
import com.example.dokumin.data.repositories.FolderRepository.countFolder
import com.example.dokumin.data.source.preferences.AppPreferences
import com.example.dokumin.databinding.FragmentHomeBinding
import com.example.dokumin.ui.document.DocumentDetailActivity
import com.shashank.sony.fancytoastlib.FancyToast

class HomeFragment : Fragment() {

    private var binding : FragmentHomeBinding? = null
    private var documentAdapter: DocumentAdapter? = null
    private val appPreferences by lazy {
        AppPreferences(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting up the welcome text with bold styling

        val fullText = "Welcome ${appPreferences.getUserName()}\nto Dokumin"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            8,
            13,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            17,
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.welcomeText?.apply {
            textSize = 22f
            text = spannable
        }

        DocumentRepository.getCountDocuments()
        DocumentRepository.getNewestDocuments()
        FolderRepository.getCountFolder()
        setupRecyclerView()
        observeListDocument()
        observeCountDocument()
        observeCountFolder()
    }

    private fun observeCountFolder() {
        countFolder.observe(viewLifecycleOwner) { it -> binding?.tvTotalFolders?.text = it?.totalCount.toString() }
    }

    private fun observeCountDocument() {
        countDocument.observe(viewLifecycleOwner) { it -> binding?.tvTotalDocs?.text = it?.totalCount.toString() }
    }


    fun onDocumentClick(doc: Document?) {
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
                    requireContext(),
                    "File type not supported",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
                DocumentRepository.selectedDocType = DocType.UNKNOWN
            }
        }

        // Navigate to DocumentDetailActivity if the file type is supported
        if(DocumentRepository.selectedDocType != DocType.UNKNOWN){
            val intent = Intent(requireActivity(), DocumentDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeListDocument() {
        newestDocumentList.observe(viewLifecycleOwner) { it ->
            documentAdapter?.setList(it ?: emptyList())
        }
        DocumentRepository.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                FancyToast.makeText(
                    requireContext(),
                    it,
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        documentAdapter = DocumentAdapter(
            onDocument = ::onDocumentClick
        )
        binding?.recyclerView?.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }



    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
