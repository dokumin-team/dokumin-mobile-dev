package com.example.dokumin.ui.folder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.adapter.FolderAdapter
import com.example.dokumin.data.folder.FolderItem

class FolderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderList = listOf(
            FolderItem("Documents"),
            FolderItem("Images"),
            FolderItem("Videos"),
            FolderItem("Music"),
            FolderItem("Downloads")
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.FolderList)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FolderAdapter(folderList)
    }
}
