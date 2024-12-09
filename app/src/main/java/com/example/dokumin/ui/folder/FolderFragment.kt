package com.example.dokumin.ui.folder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dokumin.adapter.FolderAdapter
import com.example.dokumin.data.model.responses.folder.Folder
import com.example.dokumin.data.repositories.FolderRepository
import com.example.dokumin.data.repositories.FolderRepository.folderList
import com.example.dokumin.databinding.FragmentFolderBinding
import com.shashank.sony.fancytoastlib.FancyToast

class FolderFragment : Fragment() {

    private var binding: FragmentFolderBinding? = null
    private var folderAdapter: FolderAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        FolderRepository.getFolders()
        observeListFolder()

    }

    private fun setupRecyclerView() {
        folderAdapter = FolderAdapter(
            onFolderClick = ::onFolderCLick
        )

        binding?.FolderList?.apply {
            adapter = folderAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun onFolderCLick(folder: Folder?) {
        FancyToast.makeText(
            requireContext(),
            folder?.folderName ?: "",
            FancyToast.LENGTH_SHORT,
            FancyToast.SUCCESS,
            false
        ).show()
    }

    private fun observeListFolder() {
        folderList.observe(viewLifecycleOwner){ it ->
            folderAdapter?.setList(it?.folders ?: emptyList())
        }
        FolderRepository.errorMessage.observe(viewLifecycleOwner){ error ->
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
