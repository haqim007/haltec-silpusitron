package com.haltec.silpusitron.common.util


object AllowedFileExtension {
    const val DOC_TYPE = "application/msword"
    const val DOCX_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    const val XLS_TYPE = "application/vnd.ms-excel"
    const val XLSX_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    const val PPT_TYPE = "application/vnd.ms-powerpoint"
    const val PPTX_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    const val PDF_TYPE = "application/pdf"
    const val IMAGE_TYPE = "image/*"
    const val ZIP_TYPE = "application/zip"
    const val ZIP_COMPRESSED_TYPE = "application/x-zip-compressed"
    const val RAR_TYPE = "application/vnd.rar"
    const val RAR2_TYPE = "application/rar"
    const val RAR_COMPRESSED_TYPE = "application/x-rar-compressed"
//const val TGZ_TYPE = "application/x-compressed"
//const val x7ZIP_TYPE = "application/x-7z-compressed"
//const val TAR_TYPE = "application/x-tar"
//const val AUDIO_TYPE = "audio/*"
//const val TEXT_TYPE = "text/*"
    
    val extensions = "${DOC_TYPE};" +
            "${DOCX_TYPE};" +
            "${XLS_TYPE};" +
            "${XLSX_TYPE};" +
            "${PPT_TYPE};" +
            "${PPTX_TYPE};" +
            "${PDF_TYPE};" +
            "${IMAGE_TYPE};" +
            "${ZIP_TYPE};" +
            "${RAR_TYPE};" +
            "${RAR2_TYPE};" +
            "${ZIP_COMPRESSED_TYPE};" +
            "${RAR_COMPRESSED_TYPE};"
}