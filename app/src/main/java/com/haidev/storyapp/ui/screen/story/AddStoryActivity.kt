package com.haidev.storyapp.ui.screen.story

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.haidev.storyapp.R
import com.haidev.storyapp.data.model.Status
import com.haidev.storyapp.databinding.ActivityAddStoryBinding
import com.haidev.storyapp.ui.base.BaseActivity
import com.haidev.storyapp.ui.custom.LoadingScreen
import com.haidev.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.io.File

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding, AddStoryViewModel>(),
    AddStoryNavigator {

    private val addStoryViewModel: AddStoryViewModel by inject()
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding

    private var fileImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        addStoryViewModel.navigator = this
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        initUI()
    }

    private fun initUI() {
        binding?.ivBack?.setOnClickListener {
            finish()
        }

        binding?.btnUploadCamera?.setOnClickListener {
            startTakePhoto()
        }

        binding?.btnUploadGalery?.setOnClickListener {
            startGallery()
        }
    }

    override fun onReadyAction() {
        binding?.btnSubmit?.setOnClickListener {
            if (fileImage != null && binding?.etDescription?.text.toString().isNotEmpty()) {
                val desc =
                    binding?.etDescription?.text?.toString()
                        ?.toRequestBody("text/plain".toMediaType())

                val requestImageFile = fileImage?.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("photo", fileImage?.name, requestImageFile!!)

                desc?.let { it -> addStoryViewModel.postAddStory(imageMultipart, it) }
            } else {
                Toast.makeText(this, "Please fill photo and description!", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    override fun onObserveAction() {
        addStoryViewModel.responseAddStory.observe(this, {
            when (it?.status) {
                Status.LOADING ->
                    LoadingScreen.displayLoadingWithText(this, "Upload story. . .", false)
                Status.SUCCESS -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(this, "Upload story success", Toast.LENGTH_SHORT).show()
                    backToHome()
                }
                Status.ERROR -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> LoadingScreen.hideLoading()
            }
        })
    }

    override fun setLayout(): Int = R.layout.activity_add_story

    override fun getViewModels(): AddStoryViewModel = addStoryViewModel

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "No have permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            val selectedImg: Uri = Uri.parse(
                MediaStore.Images.Media.insertImage(
                    this.contentResolver,
                    imageBitmap,
                    "Title",
                    null
                )
            )
            fileImage = uriToFile(selectedImg, this@AddStoryActivity)
            binding?.ivThumbnail?.setImageBitmap(imageBitmap)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            fileImage = uriToFile(selectedImg, this@AddStoryActivity)
            binding?.ivThumbnail?.setImageURI(selectedImg)
        }
    }

    override fun backToHome() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}