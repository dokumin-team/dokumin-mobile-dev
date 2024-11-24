package com.example.dokumin.ui.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.adapter.FileAdapter
import com.example.dokumin.data.file.FileItem
import com.example.dokumin.databinding.FragmentDocumentBinding

class DocumentFragment : Fragment() {

    private var _binding: FragmentDocumentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_document, container, false)

        val sampleData = listOf(
            FileItem("Document 1", R.drawable.docs),
            FileItem("Document 2", R.drawable.docs),
            FileItem("Document 3", R.drawable.docs),
            FileItem("Document 4", R.drawable.docs),
            FileItem("Document 5", R.drawable.docs)
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_document)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FileAdapter(sampleData)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}