package com.example.dokumin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.data.file.FileItem

class FileAdapter(private val fileList: List<FileItem>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.fileTextView)
        val fileImage: ImageView = itemView.findViewById(R.id.img_item_photo_finish)
        val fileDate: TextView = itemView.findViewById(R.id.dateTextView)
        val fileTime: TextView = itemView.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val currentItem = fileList[position]
        holder.fileName.text = currentItem.fileName
        holder.fileImage.setImageResource(currentItem.imageResId)
        holder.fileDate.text = currentItem.date
        holder.fileTime.text = currentItem.time
    }

    override fun getItemCount() = fileList.size
}
