package com.silpusitron.common.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object FileHelper {

    var FALLBACK_COPY_FOLDER: String = "upload_part"

    fun copyFileToDownloads(context: Context, downloadedFile: File): Uri? {
        val resolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, getFileName(downloadedFile.toUri()))
                put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(downloadedFile.toString()))
                put(MediaStore.MediaColumns.SIZE, downloadedFile.length())
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
//          No need to copy because there is same file path to destination path
            null
        }?.also { downloadedUri ->
            resolver.openOutputStream(downloadedUri).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream = BufferedInputStream(FileInputStream(downloadedFile.absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        }
    }

    fun getPath(context: Context, uri: Uri): String? {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {
                try {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri =
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id))
                    return getDataColumn(context, contentUri, null, null)
                }catch (e: NumberFormatException) {
                    return uri.path
                }

            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    "document" -> {
                        return copyFileToInternalStorage(context, uri, FALLBACK_COPY_FOLDER)
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        else if (uri.scheme == "msf") {
            // Extract the path from the URI
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                uri?.let {
                    context.contentResolver.query(
                        it,
                        projection,
                        selection,
                        selectionArgs,
                        null
                    )
                }
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /***
     * Used for Android Q+
     * @param uri
     * @param newDirName if you want to create a directory, you can set this variable
     * @return
     */
    private fun copyFileToInternalStorage(
        context: Context,
        uri: Uri,
        newDirName: String
    ): String {

        val returnCursor: Cursor? = context.contentResolver.query(
            uri, arrayOf(
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
            ), null, null, null
        )

        var path = ""
        returnCursor?.use {cursor ->
            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            val name = (nameIndex.let { cursor.getString(it) })
            val size = (sizeIndex.let { cursor.getLong(it).toString() })

            val output: File
            if (newDirName != "") {
                val randomCollisionAvoidance = UUID.randomUUID().toString()

                val dir = File(
                    context.filesDir
                        .toString() + File.separator + newDirName + File.separator + randomCollisionAvoidance
                )
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                output = File(
                    context.filesDir
                        .toString() +
                            File.separator + newDirName + File.separator +
                            randomCollisionAvoidance + File.separator + name
                )
            } else {
                output = File(context.filesDir.toString() + File.separator + name)
            }

            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(output)
                var read = 0
                val bufferSize = 1024
                val buffers = ByteArray(bufferSize)

                while ((inputStream?.read(buffers).also {
                        if (it != null) {
                            read = it
                        }
                    }) != -1) {
                    outputStream.write(buffers, 0, read)
                }

                inputStream?.close()
                outputStream.close()
            } catch (e: Exception) {
                Log.e("FileHelper", e.message!!)
            }

            path = output.path
        }

        return path
    }

    fun getFileSize(uri: Uri): Double {
        return uri.toFile().length().toDouble()
    }

    fun getFileSizeInMB(
        uri: Uri
    ): Double{
        val fileSizeInBytes = getFileSize(uri)
        return if (fileSizeInBytes >= 0) fileSizeInBytes / (1024.0 * 1024.0) else fileSizeInBytes
    }

    fun getMimeType(context: Context, uri: Uri): String? {
        return when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(uri)
            else -> {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
            }
        }
    }

    fun getMimeType(absolutePath: String): String? {
        val file = File(absolutePath)
        return getMimeType(file)
    }

    fun getMimeType(file: File): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.toURI().toString())
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
    }

    fun getInternalFileUri(context: Context, file: File): Uri? {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Use the same authority as in the manifest
            file
        )
    }

    fun absolutePathToUri(
        context: Context,
        absolutePath: String): Uri? {
        val file = File(absolutePath)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun getFileName(uri: Uri): String? {
        return uri.lastPathSegment
    }

    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (it != null && it.moveToFirst()) {
                    if (it.getColumnIndex(OpenableColumns.DISPLAY_NAME)>= 0){
                        result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }

                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    fun getFileNameFromAbsolutePath(filePath: String): String {
        val file = File(filePath)
        return file.name
    }

    suspend fun checkContentTypeAndOpen(context: Context, url: String) {
        val client = HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
            }
        }
        val response: HttpResponse = client.head(url)
        val contentType = response.headers[HttpHeaders.ContentType] ?: "unknown"

        when {
            contentType.startsWith("application/pdf") -> openPdf(context, url)
            contentType.startsWith("image/") -> openImage(context, url)
            else -> openInBrowser(context, url)
        }
    }

    private fun openPdf(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setDataAndType(Uri.parse(url), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)
    }

    private fun openImage(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setDataAndType(Uri.parse(url), "image/*")
        }
        context.startActivity(intent)
    }

    private fun openInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    }

}

// custom class of GetContent: input string has multiple mime types divided by ";"
// Here multiple mime type are divided and stored in array to pass to putExtra.
// super.createIntent creates ordinary intent, then add the extra.
class GetContentWithMultiFilter: ActivityResultContracts.GetContent() {
    override fun createIntent(context:Context, input:String): Intent {
        super.createIntent(context, input)
        val inputArray = input.split(";").toTypedArray()
        val myIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = AllowedFileExtension.IMAGE_TYPE
            putExtra(Intent.EXTRA_MIME_TYPES, inputArray)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        }
        return myIntent
    }
}
