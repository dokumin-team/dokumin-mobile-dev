package com.example.dokumin.ui.document

import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dokumin.R
import com.shashank.sony.fancytoastlib.FancyToast

class AddNewDocumentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_document)
        val items = listOf("Folder 1", "Folder 2", "Folder 3")
        val autoComplete : AutoCompleteTextView = findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_folder,items)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
           adapterView, view, i, l ->

            val itemSelected = adapterView.getItemAtPosition(i)
            FancyToast.makeText(
                this,
                "Item selected: $itemSelected",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                true
            ).show()

        }
    }
}