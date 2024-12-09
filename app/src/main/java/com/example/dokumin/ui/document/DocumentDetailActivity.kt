package com.example.dokumin.ui.document

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.data.source.remote.RetrofitConfig
import com.example.dokumin.databinding.ActivityDocumentDetailBinding
import com.shashank.sony.fancytoastlib.FancyToast


class DocumentDetailActivity : AppCompatActivity() {
    private var binding: ActivityDocumentDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDocumentDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupDocument()
    }

    private fun setupDocument() {
        when (DocumentRepository.selectedDocType) {
            DocType.PDF -> {
                val path=DocumentRepository.selectedDocument?.url
                binding?.wvDocumentDetail?.settings?.loadWithOverviewMode = true
                binding?.wvDocumentDetail?.settings?.javaScriptEnabled = true
                val url = "https://docs.google.com/gview?embedded=true&url=$path"
                binding?.wvDocumentDetail?.loadUrl(url)
            }

            DocType.DOC -> {
                val path=DocumentRepository.selectedDocument?.url
                binding?.wvDocumentDetail?.settings?.loadWithOverviewMode = true
                binding?.wvDocumentDetail?.settings?.javaScriptEnabled = true
                val url = "https://docs.google.com/gview?embedded=true&url=$path"
                binding?.wvDocumentDetail?.loadUrl(url)
            }

            DocType.TXT -> {
                val path=DocumentRepository.selectedDocument?.url
                binding?.wvDocumentDetail?.settings?.loadWithOverviewMode = true
                binding?.wvDocumentDetail?.settings?.javaScriptEnabled = true
                val url = "https://docs.google.com/gview?embedded=true&url=$path"
                binding?.wvDocumentDetail?.loadUrl(url)
            }

            DocType.IMAGE -> {
                binding?.ivDocumentDetail?.visibility = View.VISIBLE

                val glideUrl = GlideUrl(
                    DocumentRepository.selectedDocument?.url,
                    LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer ${RetrofitConfig.token}")
                        .build()
                )

                Glide.with(this@DocumentDetailActivity)
                    .load(glideUrl)
                    .into(binding?.ivDocumentDetail!!)

            }

            else -> {
                FancyToast.makeText(
                    this,
                    "Document type not recognized or not supported",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
                finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}