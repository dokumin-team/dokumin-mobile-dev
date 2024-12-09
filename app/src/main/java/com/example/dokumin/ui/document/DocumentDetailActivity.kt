package com.example.dokumin.ui.document

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.databinding.ActivityDocumentDetailBinding


class DocumentDetailActivity : AppCompatActivity() {
    private var binding: ActivityDocumentDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDocumentDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.wvDocumentDetail?.settings?.apply {
            loadWithOverviewMode = true
            javaScriptEnabled = true
//            useWideViewPort = true
//            builtInZoomControls = true
//            displayZoomControls = false
        }
//        binding?.wvDocumentDetail?.setInitialScale(1) // Scales the content properly

        setupDocument()
    }

    private fun setupDocument() {
        val path = DocumentRepository.selectedDocument?.url

        when (DocumentRepository.selectedDocType) {
            DocType.PDF -> {
                binding?.wvDocumentDetail?.visibility = View.VISIBLE
                setupWebViewWithGoogleDocsViewer(path)
            }

            DocType.TXT -> {
                binding?.wvDocumentDetail?.visibility = View.VISIBLE
                setupWebView(path)
            }

            DocType.IMAGE -> {
                binding?.ivDocumentDetail?.visibility = View.VISIBLE
                binding?.wvDocumentDetail?.visibility = View.GONE

                Glide.with(this@DocumentDetailActivity)
                    .load(path)
                    .into(binding?.ivDocumentDetail!!)
            }

            DocType.DOC, DocType.PPT -> {
                binding?.wvDocumentDetail?.visibility = View.VISIBLE
                setupWebViewWithGoogleDocsViewer(path)
            }

            else -> {
                binding?.wvDocumentDetail?.visibility = View.VISIBLE
                setupWebView(path)
            }
        }
    }

    private fun setupWebViewWithGoogleDocsViewer(path: String?) {
        val encodedUrl = Uri.encode(path) // Encode URL to avoid issues
        val url = "https://docs.google.com/gview?embedded=true&url=$encodedUrl"

        Log.d("WebView", "Loading URL: $url")
        setupWebView(url)
    }

    private fun setupWebView(url: String?) {
        binding?.wvDocumentDetail?.loadUrl(url ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}