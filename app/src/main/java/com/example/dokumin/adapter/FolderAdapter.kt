package com.example.dokumin.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.data.folder.FolderItem

class FolderAdapter(private val folderList: List<FolderItem>) :
    RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderImage: ImageView = itemView.findViewById(R.id.img_flower)
        val folderName: TextView = itemView.findViewById(R.id.tv_folder_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folderList[position]
        holder.folderName.text = folder.folderName
        holder.folderImage.setImageResource(R.drawable.img_folder)
    }

    override fun getItemCount(): Int = folderList.size
}
