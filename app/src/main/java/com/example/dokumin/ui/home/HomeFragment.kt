package com.example.dokumin.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dokumin.adapter.DocumentAdapter
import com.example.dokumin.data.model.responses.document.DocType
import com.example.dokumin.data.model.responses.document.Document
import com.example.dokumin.data.repositories.DocumentRepository
import com.example.dokumin.data.repositories.DocumentRepository.countDocument
import com.example.dokumin.data.repositories.DocumentRepository.newestDocumentList
import com.example.dokumin.data.repositories.FolderRepository
import com.example.dokumin.data.repositories.FolderRepository.countFolder
import com.example.dokumin.data.source.preferences.AppPreferences
import com.example.dokumin.databinding.FragmentHomeBinding
import com.example.dokumin.ui.camera.ImagePreviewActivity
import com.example.dokumin.ui.document.DocumentDetailActivity
import com.shashank.sony.fancytoastlib.FancyToast

class HomeFragment : Fragment() {

    private var binding : FragmentHomeBinding? = null
    private var documentAdapter: DocumentAdapter? = null
    private val appPreferences by lazy {
        AppPreferences(requireContext())
    }

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200
    private var photoUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fullText = "Welcome ${appPreferences.getUserName().split(" ").first()}\nto Dokumin"
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

        binding?.welcomeText?.apply {
            textSize = 22f
            text = spannable
        }

        DocumentRepository.getCountDocuments()
        DocumentRepository.getNewestDocuments()
        FolderRepository.getCountFolder()
        setupRecyclerView()
        observeListDocument()
        observeCountDocument()
        observeCountFolder()


        binding?.scanQr?.setOnClickListener {
            showImagePickerDialog(isForQrCode = true)
        }

        binding?.scanOcr?.setOnClickListener {
            showImagePickerDialog(isForQrCode = false)
        }
    }

    private fun observeCountFolder() {
        countFolder.observe(viewLifecycleOwner) { it -> binding?.tvTotalFolders?.text = it?.totalCount.toString() }
    }

    private fun observeCountDocument() {
        countDocument.observe(viewLifecycleOwner) { it -> binding?.tvTotalDocs?.text = it?.totalCount.toString() }
    }


    fun onDocumentClick(doc: Document?) {
        DocumentRepository.selectedDocument = doc
        when {
            doc?.fileType!!.contains("application/pdf") -> {
                DocumentRepository.selectedDocType = DocType.PDF
            }
            doc?.fileType!!.contains("application/doc") -> {
                DocumentRepository.selectedDocType = DocType.DOC
            }
            doc?.fileType!!.contains("text/plain") -> {
                DocumentRepository.selectedDocType = DocType.TXT
            }
            doc?.fileType!!.contains("image/") -> {
                DocumentRepository.selectedDocType = DocType.IMAGE
            }
            else -> {
                FancyToast.makeText(
                    requireContext(),
                    "File type not supported",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
                DocumentRepository.selectedDocType = DocType.UNKNOWN
            }
        }

        if(DocumentRepository.selectedDocType != DocType.UNKNOWN){
            val intent = Intent(requireActivity(), DocumentDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeListDocument() {
        newestDocumentList.observe(viewLifecycleOwner) { it ->
            documentAdapter?.setList(it ?: emptyList())
        }
        DocumentRepository.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error == null) return@observe
            error?.let {
                FancyToast.makeText(
                    requireContext(),
                    it,
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        documentAdapter = DocumentAdapter(
            onDocument = ::onDocumentClick
        )
        binding?.recyclerView?.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showImagePickerDialog(isForQrCode: Boolean) {
        openGallery(isForQrCode)
    }

    private fun openCamera(isForQrCode: Boolean) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val requestCode = if (isForQrCode) CAMERA_REQUEST_CODE else CAMERA_REQUEST_CODE + 1
        startActivityForResult(cameraIntent, requestCode)
    }

    private fun openGallery(isForQrCode: Boolean) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val requestCode = if (isForQrCode) GALLERY_REQUEST_CODE else GALLERY_REQUEST_CODE + 1
        startActivityForResult(galleryIntent, requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data
                    openImageActivity(imageUri, isForQrCode = true)
                }
                CAMERA_REQUEST_CODE + 1, GALLERY_REQUEST_CODE + 1 -> {
                    val imageUri = data?.data
                    openImageActivity(imageUri, isForQrCode = false)
                }
            }
        }
    }

    private fun openImageActivity(imageUri: Uri?, isForQrCode: Boolean) {
        val intent = Intent(
            requireContext(),
            if (isForQrCode) QrCodeActivity::class.java else OcrActivity::class.java
        )
        val bundle = Bundle()
        bundle.putParcelable("imageUri", imageUri)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
