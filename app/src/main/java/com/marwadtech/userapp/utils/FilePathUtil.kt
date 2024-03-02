package com.marwadtech.userapp.utils

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.marwadtech.userapp.R
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Objects

class FilePathUtil {

    companion object {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun getPath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    // This is for checking Main Memory
                    return if ("primary".equals(type, ignoreCase = true)) {
                        if (split.size > 1) {
                            Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        } else {
                            Environment.getExternalStorageDirectory().toString() + "/"
                        }
                        // This is for checking SD Card
                    } else {
                        "storage" + "/" + docId.replace(":", "/")
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    val fileName1 = getFilePath(context, uri)
//                    if (fileName1 != null) {
//
//                            return Environment.getExternalStorageDirectory()
//                                .toString() + "/Download/" + fileName1
//
//
//                    }
                    val id = DocumentsContract.getDocumentId(uri)
                    /*final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);*/if (id != null && id.startsWith(
                            "raw:"
                        )
                    ) {
                        return id.substring(4)
                    }
                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://com.google.android.apps.docs.storage/document"
                    )
                    for (contentUriPrefix in contentUriPrefixesToTry) {
                        try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse(contentUriPrefix),
                                java.lang.Long.valueOf(id)
                            )
                            val path: String? =
                                getDataColumn(context, contentUri, null, null)
                            if (path != null) {
                                return path
                            }
                        } catch (ignored: Exception) {
                        }
                    }

                    // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                    val fileName = getFileName(context, uri)
                    val cacheDir = getDocumentCacheDir(context)
                    val file = generateFileName(fileName, cacheDir)
                    var destinationPath: String? = null
                    if (file != null) {
                        destinationPath = file.absolutePath
                        saveFileFromUri(context, uri, destinationPath)
                    }
                    return destinationPath
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        Log.d("eee", "in image")
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        Log.d("eee", "in video")
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        Log.d("eee", "in audio")
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    } else {
                        Log.d("eee", "in else")
                        return createTempFile(context, uri)
//                        contentUri = MediaStore.Files.getContentUri("external");
                        // val `is`: InputStream? = context.contentResolver.openInputStream(uri)

                        // Todo: Handle other media type here
                    }

                    Log.e("contentUri==>", contentUri.toString())

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        selection,
                        selectionArgs
                    )
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment()
                }
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        private fun requestPermission(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(
                            java.lang.String.format(
                                "package:%s",
                                context.packageName
                            )
                        )
                    (context as Activity).startActivityForResult(intent, 2296)
                } catch (e: java.lang.Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    (context as Activity).startActivityForResult(intent, 2296)
                }
            } else {
                // below android 11
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2296
                )
            }
        }

        fun createTempFile(context: Context, uri: Uri): String {
            Log.d("eee", "in temp file" + getMimeType(context, uri))

            var `in`: InputStream? = null
            var out: OutputStream? = null

            var imgFile: File? = null
            val destPath: String? = Objects.requireNonNull(
                Objects.requireNonNull(context).getExternalFilesDir(null)
            )?.absolutePath
            val imagesFolder = File(destPath, context.resources.getString(R.string.app_name))
            try {
                imagesFolder.mkdirs()
                imgFile =
                    File(imagesFolder, Date().time.toString() + "." + getMimeType(context, uri))

                `in` = context.contentResolver.openInputStream(uri)
                out = FileOutputStream(imgFile)
                val buf = ByteArray(1024)
                var len: Int
                while (`in`!!.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                out.close()
                `in`.close()

                Log.d("eee", "in temp file" + imgFile.path)
                Log.e("Second", "Return")
                return imgFile.path
            } catch (e: Exception) {
                Log.d("eee", "in catch")
                e.printStackTrace()
                out?.close()
                `in`?.close()
            } finally {
                out?.close()
                `in`?.close()
            }
            return ""
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        private fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String?) {
            var `is`: InputStream? = null
            var bos: BufferedOutputStream? = null
            try {
                `is` = context.contentResolver.openInputStream(uri)
                bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
                val buf = ByteArray(1024)
                `is`!!.read(buf)
                do {
                    bos.write(buf)
                } while (`is`.read(buf) != -1)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                    bos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        private fun generateFileName(fName: String?, directory: File?): File? {
            var name = fName ?: return null
            var file = File(directory, name)
            if (file.exists()) {
                var fileName = name
                var extension = ""
                val dotIndex = name.lastIndexOf('.')
                if (dotIndex > 0) {
                    fileName = name.substring(0, dotIndex)
                    extension = name.substring(dotIndex)
                }
                var index = 0
                while (file.exists()) {
                    index++
                    name = "$fileName($index)$extension"
                    file = File(directory, name)
                }
            }
            try {
                if (!file.createNewFile()) {
                    return null
                }
            } catch (e: IOException) {
                // Log.w(TAG, e);
                return null
            }

            // logDir(directory);
            return file
        }

        fun getDocumentCacheDir(context: Context): File {
            val dir = File(context.cacheDir, DOCUMENTS_DIR)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            //        logDir(context.getCacheDir());
            //        logDir(dir);
            return dir
        }

        const val DOCUMENTS_DIR = "documents"

        fun getName(filename: String?): String? {
            if (filename == null) {
                return null
            }
            val index = filename.lastIndexOf('/')
            return filename.substring(index + 1)
        }

        fun getMimeType(context: Context, uri: Uri): String? {
            // Check uri format to avoid null
            return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                // If scheme is a content
                val mime = MimeTypeMap.getSingleton()
                mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
            } else {
                // If scheme is a File
                // This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
                MimeTypeMap.getFileExtensionFromUrl(
                    Uri.fromFile(File(uri.path.toString())).toString()
                )
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun getFileName(context: Context, uri: Uri): String? {
            val mimeType = context.contentResolver.getType(uri)
            var filename: String? = null
            if (mimeType == null && context != null) {
                val path = getPath(context, uri)
                filename = if (path == null) {
                    getName(uri.toString())
                } else {
                    val file = File(path)
                    file.name
                }
            } else {
                val returnCursor = context.contentResolver.query(uri, null, null, null, null)
                if (returnCursor != null) {
                    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    returnCursor.moveToFirst()
                    filename = returnCursor.getString(nameIndex)
                    returnCursor.close()
                }
            }
            return filename
        }

        fun getFileSize(context: Context, uri: Uri): Long? {
            val mimeType = context.contentResolver.getType(uri)
            var fileSize: Long? = null
            if (mimeType == null && context != null) {
                val path = getPath(context, uri)
                fileSize = if (path == null) {
                    return null
                } else {
                    val file = File(path)
                    file.length()
                }
            } else {
                val returnCursor = context.contentResolver.query(uri, null, null, null, null)
                if (returnCursor != null) {
                    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                    returnCursor.moveToFirst()
                    fileSize = returnCursor.getLong(sizeIndex)
                    returnCursor.close()
                }
            }
            return fileSize
        }

        /**
         * Show toast of  message
         */
        fun Context.showToast(message: String?) {
            Toast.makeText(this, message ?: "", Toast.LENGTH_LONG).show()
        }

        /**
         * Check Internet is connected or not
         */
        fun isNetworkConnected(context: Context?): Boolean {
            val connectivityManager: ConnectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    val ni = connectivityManager.activeNetworkInfo
                    if (ni != null) {
                        return ni.isConnected && (
                            ni.type == ConnectivityManager.TYPE_WIFI ||
                                ni.type == ConnectivityManager.TYPE_MOBILE
                            )
                    }
                } else {
                    val network: Network? = connectivityManager.activeNetwork
                    if (network != null) {
                        val networkCapabilities: NetworkCapabilities? =
                            connectivityManager.getNetworkCapabilities(network)

                        return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            }
            return false
        }

        /**
         * Share string Message To Other Apps
         */
        fun shareMessageToOtherApps(mContext: Context, message: String, extraMessage: String) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, extraMessage)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
            mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        /*fun isPasswordValid(pwd: String): Boolean {
        val matcher: Matcher = VALID_PASSWORD_REGEX.matcher(pwd)
        return matcher.find()
    }*/

        fun isPasswordValid(pwd: String): Boolean {
            return pwd.length >= 6
        }

        fun formatDecimal(doubleValue: Double?): String {
            val numberFormat: NumberFormat = DecimalFormat("##.##")
            return "$" + numberFormat.format(doubleValue)
        }

        fun copyUriToFile(context: Context, uri: Uri): File? {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            var outFile: File? = null
            try {
                if (context.contentResolver != null) {
                    `in` = context.contentResolver.openInputStream(uri)
                    val path: String = "temp_image_2_" + System.currentTimeMillis() + ".jpg"
                    outFile = createImageFile(context, path)
                    if (outFile != null && `in` != null) {
                        out = FileOutputStream(outFile)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (`in`.read(buf).also { len = it } > 0) {
                            out.write(buf, 0, len)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally { // Ensure that the InputStreams are closed even if there's an exception.
                try {
                    out?.close()
                    // If you want to close the "in" InputStream yourself then remove this
                    // from here but ensure that you close it yourself eventually.
                    `in`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return outFile
        }

        @Throws(IOException::class)
        fun createDocumentFile(context: Context, fileName: String): File? {
            var storageDir = context.filesDir
            val dirCreated: Boolean
            if (storageDir == null) {
                val externalStorage =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                if (externalStorage == null) {
                    storageDir =
                        File(context.cacheDir, Environment.DIRECTORY_DOCUMENTS)
                    dirCreated = storageDir.exists() || storageDir.mkdirs()
                } else {
                    dirCreated = true
                }
            } else {
                storageDir =
                    File(context.filesDir, Environment.DIRECTORY_DOCUMENTS)
                dirCreated = storageDir.exists() || storageDir.mkdirs()
            }
            return if (dirCreated) {
                val imageFile = File(storageDir, fileName)
                var isDeleted = true
                if (imageFile.exists()) {
                    isDeleted = imageFile.delete()
                }
                if (isDeleted) {
                    val fileCreated = imageFile.createNewFile()
                    if (fileCreated) imageFile else null
                } else {
                    null
                }
            } else {
                null
            }
        }

        @Throws(IOException::class)
        fun createImageFile(context: Context, imageFileName: String): File? {
            var storageDir = context.filesDir
            val dirCreated: Boolean
            if (storageDir == null) {
                val externalStorage =
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (externalStorage == null) {
                    storageDir =
                        File(context.cacheDir, Environment.DIRECTORY_PICTURES)
                    dirCreated = storageDir.exists() || storageDir.mkdirs()
                } else {
                    dirCreated = true
                }
            } else {
                storageDir =
                    File(context.filesDir, Environment.DIRECTORY_PICTURES)
                dirCreated = storageDir.exists() || storageDir.mkdirs()
            }
            return if (dirCreated) {
                val imageFile = File(storageDir, imageFileName)
                var isDeleted = true
                if (imageFile.exists()) {
                    isDeleted = imageFile.delete()
                }
                if (isDeleted) {
                    val fileCreated = imageFile.createNewFile()
                    if (fileCreated) imageFile else null
                } else {
                    null
                }
            } else {
                null
            }
        }
        fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver.query(
                    uri!!,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(columnIndex)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun getFilePath(context: Context, uri: Uri?): String? {
            var cursor: Cursor? = null
            val projection = arrayOf(
                MediaStore.MediaColumns.DISPLAY_NAME
            )
            try {
                if (uri == null) return null
                cursor = context.contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }
    }
}
