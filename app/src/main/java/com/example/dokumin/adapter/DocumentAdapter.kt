package com.example.dokumin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.data.document.DocumentItem

class DocumentAdapter(private val documentList: List<DocumentItem>) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val documentName: TextView = itemView.findViewById(R.id.documentTextView)
        val documentImage: ImageView = itemView.findViewById(R.id.img_item_photo_docs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val currentItem = documentList[position]
        holder.documentName.text = currentItem.documentName
        if (currentItem.documentImageResId != 0) {
            holder.documentImage.setImageResource(currentItem.documentImageResId)
        }
    }

    override fun getItemCount() = documentList.size
}