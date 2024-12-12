package com.example.dokumin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.databinding.ItemDocumentBinding

class DocumentAdapter(
    val onDocument: (document: Document?) -> Unit
) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {
    private var documentList = listOf<Document?>()
    fun setList(documentList: List<Document?>) {
        this.documentList = documentList
        notifyDataSetChanged()
    }

    inner class DocumentViewHolder(val binding: ItemDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemDocument: Document?) {
            binding.imgItemPhotoDocs.setImageResource(R.drawable.docs)
            binding.tvDocumentName.text = itemDocument?.fileName
            binding?.dateTextView?.text =
                itemDocument?.createdAt?.getCreatedAtFormatted()?.split(" ")?.get(0)
            binding?.timeTextView?.text =
                itemDocument?.createdAt?.getCreatedAtFormatted()?.split(" ")?.get(1)
            binding.root.setOnClickListener {
                onDocument(itemDocument)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = ItemDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val folder = documentList[position]
        holder.bind(folder)
    }

    override fun getItemCount(): Int = documentList.size
}