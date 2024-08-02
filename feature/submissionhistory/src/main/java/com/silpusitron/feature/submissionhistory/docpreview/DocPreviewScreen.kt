package com.silpusitron.feature.submissionhistory.docpreview

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.silpusitron.common.util.AllowedFileExtension
import com.silpusitron.common.util.FileHelper
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.SimpleTopAppBar
import com.haltec.silpusitron.core.ui.parts.error.ErrorView
import com.haltec.silpusitron.core.ui.parts.loading.LoadingView
import com.haltec.silpusitron.core.ui.util.PermissionRequester
import com.haltec.silpusitron.core.ui.util.isPermissionGranted
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.submissionhistory.common.domain.SubmissionHistory
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun DocPreviewScreen(
    modifier: Modifier = Modifier,
    history: SubmissionHistory,
    viewModel: DocPreviewViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val action = {action: DocPreviewUiAction -> viewModel.doAction(action)}
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        action(DocPreviewUiAction.GetPDF(history))
    }
    val scope = rememberCoroutineScope()

    var downloadUri: Uri? by remember {
        mutableStateOf(null)
    }

    var openDownloadDialog by remember {
        mutableStateOf(false)
    }


    if (openDownloadDialog){
        AlertDialog(
            onDismissRequest = {openDownloadDialog = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            setDataAndType(downloadUri, "*/*")
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        context.startActivity(intent)
                        openDownloadDialog = false
                    }
                ) {
                    Text(text = stringResource(id = CoreR.string.open_folder))
                }
            },
            icon = {
                LottieLoader(
                    jsonRaw = CoreR.raw.lottie_success_sparkle,
                    modifier = Modifier.size(150.dp),
                    iterations = 1
                )
            },
            text = {
                Text(stringResource(id = CoreR.string.download_complete))
            }
        )
    }

    // for android below R
    var writeStoragePermissionDenied by remember {
        mutableStateOf(false)
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

        val permissions = mutableListOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        PermissionRequester(
            permissions = permissions,
            onPermissionGranted = {
                writeStoragePermissionDenied = false
            },
            onPermissionDenied = {
                writeStoragePermissionDenied = true
            }
        )

        if (writeStoragePermissionDenied){
            AlertDialog(
                onDismissRequest = { writeStoragePermissionDenied = false },
                confirmButton = {
                    TextButton(onClick = { writeStoragePermissionDenied = false }) {
                        Text(text = stringResource(id = CoreR.string.ok))
                    }
                },
                icon = {
                    LottieLoader(
                        jsonRaw = CoreR.raw.lottie_error,
                        modifier = Modifier.size(150.dp),
                        iterations = 1
                    )
                },
                text = {
                    Text(stringResource(id = CoreR.string.folder_access_permit_required))
                }
            )
        }
    }


    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopAppBar(
                title = history.title,
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            state.downloadFileResult.data?.let {
                Column {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.background,
                        onClick = {
                            val file = state.downloadFileResult.data
                            val fileUri = file?.let {
                                FileHelper.getInternalFileUri(context, it)
                            } ?: return@FloatingActionButton
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(fileUri, AllowedFileExtension.PDF_TYPE)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant temporary read permission
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = stringResource(id = CoreR.string.open)
                        )
                    }

                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.background,
                        onClick = {

                            if (
                                Build.VERSION.SDK_INT < Build.VERSION_CODES.R &&
                                !isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            ) {
                                writeStoragePermissionDenied = true
                                return@FloatingActionButton
                            }

                            scope.launch {
                                val copyFile = async(Dispatchers.IO){
                                    FileHelper.copyFileToDownloads(context, it)
                                }
                                downloadUri = copyFile.await()
                                openDownloadDialog = true
                            }

                        },
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = stringResource(id = CoreR.string.download)
                        )
                    }
                }
            }
        }
    ) { contentPadding ->

        Box(Modifier.padding(contentPadding)) {
            when(state.downloadFileResult){
                is Resource.Loading -> {
                    LoadingView(
                        loader = {
                            LottieLoader(
                                jsonRaw = CoreR.raw.lottie_file_loading,
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .size(200.dp)
                            )
                        },
                    )
                }

                is Resource.Error -> {
                    ErrorView(
                        message = state.downloadFileResult.message ?:
                            stringResource(id = CoreR.string.unknown_error),
                        onTryAgain = {
                            action(DocPreviewUiAction.GetPDF(history))
                        }
                    )
                }
                is Resource.Success -> {
                    state.downloadFileResult.data?.let {
                        PdfRendererViewCompose(
                            file = it,
                            lifecycleOwner = LocalLifecycleOwner.current,
                            statusCallBack = null
                        )
                    }
                        ?:
                        ErrorView(
                            message = stringResource(id = CoreR.string.failed_to_load_pdf),
                            onTryAgain = {
                                action(DocPreviewUiAction.GetPDF(history))
                            }
                        )

                }
                is Resource.Idle -> Unit
            }
        }

    }
}