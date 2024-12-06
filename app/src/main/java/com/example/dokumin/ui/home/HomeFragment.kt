package com.example.dokumin.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dokumin.R
import com.example.dokumin.adapter.FileAdapter
import com.example.dokumin.data.file.FileItem

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Setting up the welcome text with bold styling
        val welcomeTextView = root.findViewById<TextView>(R.id.welcomeText)
        val fullText = "Welcome Cinta\nto Dokumin"
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

        welcomeTextView.textSize = 22f
        welcomeTextView.text = spannable

        val sampleData = listOf(
            FileItem("Document 1", R.drawable.docs),
            FileItem("Document 2", R.drawable.docs),
            FileItem("Document 3", R.drawable.docs),
        )

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FileAdapter(sampleData)

        return root
    }
}
