package com.example.dokumin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.data.model.responses.folder.Folder
import com.example.dokumin.databinding.ItemFolderBinding

class FolderAdapter(
    val onFolderClick: (folder: Folder?) -> Unit
) :
    RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {
    private var folderList = listOf<Folder?>()

    fun setList(folderList: List<Folder?>) {
        this.folderList = folderList
        notifyDataSetChanged()
    }

    inner class FolderViewHolder(val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemFolder: Folder?) {
            binding.imgFolder.setImageResource(R.drawable.img_folder)
            binding.tvFolderName.text = itemFolder?.folderName
            binding.root.setOnClickListener {
                onFolderClick(itemFolder)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
        val view = ItemFolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folderList[position]
        holder.bind(folder)
    }

    override fun getItemCount(): Int = folderList.size
}
