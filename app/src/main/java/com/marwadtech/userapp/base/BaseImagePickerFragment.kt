package com.marwadtech.userapp.base

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.utils.FilePathUtil
import com.marwadtech.userapp.utils.FilePathUtils.Companion.showToast
import com.marwadtech.userapp.utils.ImageSelection
import com.marwadtech.userapp.utils.RequestKey
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
open class BaseImagePickerFragment : BaseFragment() {
    var path = ArrayList<Uri>()
    lateinit var uriPathListener: UriPathListener

    var photoFile: File? = null
    var hasBrowser: Boolean = false

    private var fileUri: MutableLiveData<Uri?> = MutableLiveData()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//        if (::uriPathListener.isInitialized) uri?.let { uriPathListener.imagePath(it) }
            val file = File(requireActivity().cacheDir, "IMG_${System.currentTimeMillis()}")
            uri?.let {
                UCrop.of(it, Uri.fromFile(file))
                    .start(requireContext(), this@BaseImagePickerFragment)
            }
            Log.e(TAG, "onActivityResult: $uri")
        }

    private val pdf = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        if (::uriPathListener.isInitialized) uri?.let { uriPathListener.imagePath(it) }

        Log.e(TAG, "onActivityResult: $uri")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            findNavController().navigate(
                R.id.action_global_imageSelectionFragment,
                bundleOf("hasBrowser" to hasBrowser)
            )
            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // feature requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        fileUri.observe(viewLifecycleOwner) {
            if (it != null) {
                cropImage(it)
                fileUri.value = null
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(RequestKey.IMAGE_SELECTION) { _, bundle ->
            bundle.getInt(RequestKey.IMAGE_SELECTION).apply {
                when (this) {
                    ImageSelection.Camera -> {
                        Log.e(TAG, "onCreate: IMAGE_SELECTION")
                        if (!checkPermissions(permissionsList = REQUIRED_PERMISSIONS)) {
                            requestPermissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        } else {
                            pickImage(true)
                        }
                    }

                    ImageSelection.Gallery -> {
                        Log.e(TAG, "onCreate: IMAGE_SELECTION")
                        if (isPhotoPickerAvailable()) {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        } else {
                            if (!checkPermissions(permissionsList = REQUIRED_PERMISSIONS)) {
                                requestPermissionLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                            } else {
                                pickImage(false)
                            }
                        }
                    }

                    ImageSelection.Browser -> {
                        Log.e(TAG, "onCreate: IMAGE_SELECTION")
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "application/pdf"
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        startActivityForResult(intent, 300)
                    }
                }
            }
            clearFragmentResult(RequestKey.IMAGE_SELECTION)
        }
    }

    private fun pickImage(hasCamera: Boolean = true) {
        TedImagePicker.with(requireContext()).showCameraTile(hasCamera).image().start {
            fileUri.postValue(it)
        }
    }

    private fun cropImage(uri: Uri) {
        val file = File(
            requireContext().cacheDir,
            "IMG_${System.currentTimeMillis()}"
        )
        UCrop.of(uri, Uri.fromFile(file)).start(
            requireContext(),
            this@BaseImagePickerFragment
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun selectMediaType(hasBrowser: Boolean = false, hasGallery: Boolean = true) {
        if (!checkPermissions(permissionsList = REQUIRED_PERMISSIONS)) {
            this.hasBrowser = hasBrowser
            requestPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        } else {
            findNavController().navigate(
                R.id.action_global_imageSelectionFragment,
                bundleOf("hasBrowser" to hasBrowser, "hasGallery" to hasGallery)
            )
        }
    }

//    fun openPhotoPicker() {
//        if (isPhotoPickerAvailable()) {
//            findNavController().navigate(R.id.action_global_imageSelectionFragment)
// //            requestCameraPermissions()
//        } else {
//            if (!checkPermissions(permissionsList = REQUIRED_PERMISSIONS)) {
//                requestPermissionLauncher.launch(
//                    Manifest.permission.CAMERA
//                )
//            } else {
//                TedImagePicker.with(requireActivity()).image().start {
//                    var file = File(requireActivity().cacheDir, "IMG_${System.currentTimeMillis()}")
//                    UCrop.of(it, Uri.fromFile(file))
//                        .start(requireContext(), this@BaseImagePickerFragment)
//                }
//            }
//        }
//    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            e.localizedMessage?.let { Log.e(TAG, it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                val uri = data?.data
//                if (::uriPathListener.isInitialized) uri?.let { uriPathListener.imagePath(it) }
                var file = File(requireActivity().cacheDir, "IMG_${System.currentTimeMillis()}")
                uri?.let {
                    UCrop.of(it, Uri.fromFile(file))
                        .start(requireContext(), this@BaseImagePickerFragment)
                }
                Log.e(TAG, "onActivityResult: $uri")
            }

            2000 -> {
                /*data?.extras?.get("data")*/photoFile?.apply {
//                    val image: Bitmap = this as Bitmap
//                    val data = MultipartUtils.convertBitmapToFile(
//                        requireActivity(), System.currentTimeMillis().toString(), image
//                    )
                    if (::uriPathListener.isInitialized) {
                        data.let {
                        /*uriPathListener.imagePath(
                            Uri.fromFile(it)
                        )*/
                            var file = File(
                                requireActivity().cacheDir,
                                "IMG_${System.currentTimeMillis()}"
                            )
                            UCrop.of(Uri.fromFile(this), Uri.fromFile(file))
                                .start(requireContext(), this@BaseImagePickerFragment)
                        }
                    }
                }
            }

            300 -> {
                /**
                 * pdf file
                 */
                lifecycleScope.launch {
                    data?.data?.let { uri ->
                        // val file = File(FilePathUtil.getPath(requireActivity(), uri))
//                        if (checkFileSize(file) > 10240L) {
//                            requireActivity().showToast(getString(R.string.file_can_not_use_more_then_10mb))
//                        } else {
//                            if (::uriPathListener.isInitialized) uriPathListener.browserPath(uri)
//                        }
                        val file: File? = getFileFromUri(requireActivity(), uri)
                        file?.apply {
                            if (checkFileSize(this) > 10240L) {
                                requireActivity().showToast(getString(R.string.file_can_t_use_more_then_10mb))
                            } else {
                                if (::uriPathListener.isInitialized) uriPathListener.browserPath(uri)
                            }
                        } ?: run {
                            requireActivity().showToast("pdf path issues")
                        }
                    }
                }
            }

            UCrop.REQUEST_CROP -> {
                if (data == null) {
                    // requireActivity().showToast("Error Invalid Image")
                    return
                }
                val uri = UCrop.getOutput(data)
                if (::uriPathListener.isInitialized) {
                    data.let {
                        if (uri != null) {
                            lifecycleScope.launch {
                                val file = File(FilePathUtil.getPath(requireContext(), uri))

                                if (checkFileSize(file) > 10240L) {
                                    requireActivity().showToast(getString(R.string.file_can_t_use_more_then_10mb))
                                } else {
                                    Log.e(TAG, "onActivityResult: ${checkFileSize(file)}")
                                    val compressedFile = Compressor.compress(
                                        requireContext(),
                                        file
                                    )
                                    Log.e(TAG, "onActivityResult: ${checkFileSize(compressedFile)}")
                                    uriPathListener.imagePath(Uri.fromFile(compressedFile))
                                }
                            }
                        }
                    }
                }
            }

            UCrop.RESULT_ERROR -> {
                requireActivity().showToast("Image Result Error")
            }
        }
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: 0)
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val file = File(context.cacheDir, displayName)
                    try {
                        val outputStream = FileOutputStream(file)
                        val buffer = ByteArray(4 * 1024) // Adjust the buffer size as per your requirement
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                        outputStream.close()
                        inputStream.close()
                        return file
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return null
    }

    private fun checkFileSize(file: File): Long {
        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        return fileSizeInBytes / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
    }

    private fun isPhotoPickerAvailable(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun requestCameraPermissions() {
        if (!checkPermissions(REQUIRED_PERMISSIONS)) {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                100
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun checkPermissions(permissionsList: Array<String>): Boolean {
        permissionsList.forEach {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    companion object {
        private val TAG: String = BaseImagePickerFragment::class.java.name
        private const val CROP_PIC = 1000
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_IMAGE_CAPTURE = 1
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    interface UriPathListener {
        fun imagePath(path: Uri)
        fun browserPath(path: Uri)
    }
}
