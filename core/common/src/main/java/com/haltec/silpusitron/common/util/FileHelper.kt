package com.haltec.silpusitron.common.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object FileHelper {

    var FALLBACK_COPY_FOLDER: String = "upload_part"

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
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
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

    fun absolutePathToUri(
        context: Context,
        applicationId: String,
        absolutePath: String): Uri? {
        val file = File(absolutePath)
        return FileProvider.getUriForFile(
            context,
            "$applicationId.fileprovider", // Replace with your FileProvider authority
            file
        )
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        return when (uri.scheme) {
            "content" -> {
                val cursor = context.contentResolver.query(
                    uri, null, null,
                    null, null
                )
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        it.getString(nameIndex)
                    } else {
                        null
                    }
                }
            }
            "file" -> uri.lastPathSegment
            else -> null
        }
    }

    fun getFileNameFromAbsolutePath(filePath: String): String {
        val file = File(filePath)
        return file.name
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
