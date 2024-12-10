package com.example.dokumin.ui.document

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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

        // setting up webview
        val webView = binding?.wvDocumentDetail

        val webSettings = webView?.settings
        webSettings?.javaScriptEnabled = true
        webSettings?.loadWithOverviewMode = true
        webSettings?.useWideViewPort = true

        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                // Return false to allow the WebView to handle the URL
                return false
            }
        }
        setupDocument()

    }

    private fun setupDocument() {
        val path = DocumentRepository.selectedDocument?.url
        Log.d("DocumentDetailActivity", "setupDocument: $path")
        when (DocumentRepository.selectedDocType) {

            DocType.IMAGE -> {
                binding?.ivDocumentDetail?.visibility = View.VISIBLE
                binding?.wvDocumentDetail?.visibility = View.GONE

                Glide.with(this@DocumentDetailActivity)
                    .load(path)
                    .into(binding?.ivDocumentDetail!!)
            }

            else -> {
                binding?.wvDocumentDetail?.visibility = View.VISIBLE
                binding?.ivDocumentDetail?.visibility = View.GONE
                setupWebViewWithUrl(binding?.wvDocumentDetail, path.toString())
            }
        }
    }

    // This function configures the WebView to display the PDF.
    private fun setupWebViewWithUrl(webView: WebView?, url: String) {


            webView?.webChromeClient = object : WebChromeClient() {}

            val newurl = "https://docs.google.com/gview?embedded=true&url=$url"
            val htmlContent = getHtmlLayout(newurl)

            webView?.loadDataWithBaseURL(newurl, htmlContent, "text/html", "utf-8", null)


    }


    private fun getHtmlLayout(url: String): String {
        return """<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <style>
        body, html {
            margin: 0;
            height: 100%;
            overflow: hidden;
        }
        iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: none;
        }
    </style>
</head>
<body>
    <iframe src="$url" allow="autoplay"></iframe>
</body>
</html>"""
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}