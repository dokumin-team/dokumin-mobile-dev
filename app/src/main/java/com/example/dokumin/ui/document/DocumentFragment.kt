package com.example.dokumin.ui.document

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dokumin.adapter.DocumentAdapter
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.data.repositories.DocumentRepository.documentList
import com.example.dokumin.databinding.FragmentDocumentBinding
import com.shashank.sony.fancytoastlib.FancyToast

class DocumentFragment : Fragment() {

    private var binding: FragmentDocumentBinding? = null
    private var documentAdapter: DocumentAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDocumentBinding.inflate(inflater, container, false)

        binding?.btnAddDoc?.setOnClickListener {
            val intent = Intent(requireContext(), AddNewDocumentActivity::class.java)
            startActivity(intent)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        DocumentRepository.getDocuments()
        observeListDocument()

    }

    private fun setupRecyclerView() {
       documentAdapter = DocumentAdapter(
           onDocument = ::onDocumentCLick
       )

        binding?.rvDocument?.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onDocumentCLick(doc: Document?) {
        DocumentRepository.selectedDocument = doc
        when {
            doc?.fileType!!.contains("application/pdf") -> {
                DocumentRepository.selectedDocType = DocType.PDF
            }
            doc?.fileType!!.contains("text/plain") -> {
                DocumentRepository.selectedDocType = DocType.TXT
            }
            doc?.fileType!!.contains("image/") -> {
                DocumentRepository.selectedDocType = DocType.IMAGE
            }
            doc?.fileType!!.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document") -> {
                DocumentRepository.selectedDocType = DocType.DOC
            }
            doc?.fileType!!.contains("application/vnd.openxmlformats-officedocument.presentationml.presentation") -> {
                DocumentRepository.selectedDocType = DocType.PPT
            }
            else -> {
                DocumentRepository.selectedDocType = DocType.UNKNOWN
            }
        }


            val intent = Intent(requireActivity(), DocumentDetailActivity::class.java)
            startActivity(intent)


    }

    private fun observeListDocument() {
        documentList.observe(viewLifecycleOwner) { it ->
            documentAdapter?.setList(it ?: emptyList())
        }
        DocumentRepository.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error == null) return@observe
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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

    }
}