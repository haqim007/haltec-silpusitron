package com.haltec.silpusitron.feature.officertask.docapproval

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.haltec.silpusitron.common.util.FileHelper
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.parts.SimpleTopAppBar
import com.haltec.silpusitron.core.ui.parts.SubmitSuccessView
import com.haltec.silpusitron.core.ui.parts.dialog.DialogError
import com.haltec.silpusitron.core.ui.parts.dialog.DialogLoadingDocView
import com.haltec.silpusitron.core.ui.parts.error.ErrorView
import com.haltec.silpusitron.core.ui.parts.getAppTextFieldColors
import com.haltec.silpusitron.core.ui.parts.loading.LoadingView
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.util.KoinPreviewWrapper
import com.haltec.silpusitron.core.ui.util.PermissionRequester
import com.haltec.silpusitron.core.ui.util.isPermissionGranted
import com.haltec.silpusitron.data.mechanism.Resource
import com.haltec.silpusitron.feature.officertask.common.di.officerTaskModules
import com.haltec.silpusitron.feature.officertask.common.ui.DialogRejectingForm
import com.haltec.silpusitron.feature.officertask.common.ui.DialogSigningForm
import com.haltec.silpusitron.feature.officertask.common.ui.SigningResultView
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetter
import com.haltec.silpusitron.feature.officertask.tasks.domain.SubmittedLetterDummies
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.IOException
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun DocApprovalScreen(
    modifier: Modifier = Modifier,
    submittedLetter: SubmittedLetter,
    viewModel: DocPreviewApprovalViewModel = koinViewModel(),
    onNavigateBack: (shouldRefreshTask: Boolean) -> Unit
){
    val state by viewModel.state.collectAsState()
    val action = {action: DocPreviewApprovalUiAction -> viewModel.doAction(action)}
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        if (submittedLetter.fileUrl != null){
            action(DocPreviewApprovalUiAction.GetPDF(
                submittedLetter.title,
                submittedLetter.fileUrl
            ))
        }
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

    var isRejecting by remember {
        mutableStateOf(false)
    }

    var isSigning by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopAppBar(
                title = submittedLetter.title,
                onNavigateBack = { onNavigateBack(false) },
                prependContent = { prependContentModifier ->
                    state.downloadFileResult.data?.let {
                        IconButton(
                            modifier = prependContentModifier,
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                                if (
                                    Build.VERSION.SDK_INT < Build.VERSION_CODES.R &&
                                    !isPermissionGranted(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                ) {
                                    writeStoragePermissionDenied = true
                                    return@IconButton
                                }

                                scope.launch {
                                    val copyFile = async(Dispatchers.IO) {
                                        FileHelper.copyFileToDownloads(context, it)
                                    }
                                    downloadUri = copyFile.await()
                                    openDownloadDialog = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = stringResource(
                                    id = CoreR.string.download
                                ),
                            )
                        }
                    }
                }
            )
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
                            if (submittedLetter.fileUrl != null){
                                action(DocPreviewApprovalUiAction.GetPDF(
                                    submittedLetter.title,
                                    submittedLetter.fileUrl
                                ))
                            }
                        }
                    )
                }
                is Resource.Success -> {
                    state.downloadFileResult.data?.let {
                        Column(Modifier.fillMaxSize()) {

                            if (LocalInspectionMode.current){
                                Spacer(modifier = Modifier.weight(10f))
                            }else{
                                PdfRendererViewCompose(
                                    modifier = Modifier.weight(10f),
                                    file = it,
                                    lifecycleOwner = LocalLifecycleOwner.current,
                                    statusCallBack = null
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.SpaceAround
                            ){
                                Button(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(5.dp),
                                    colors = ButtonDefaults.buttonColors().copy(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ),
                                    onClick = { isRejecting = true }
                                ) {
                                    Text(text = stringResource(id = CoreR.string.reject))
                                }
                                Button(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(5.dp),
                                    onClick = { isSigning = true }
                                ) {
                                    Text(text = stringResource(id = CoreR.string.sign))
                                }
                            }
                        }
                    }
                        ?:
                        ErrorView(
                            message = stringResource(id = CoreR.string.failed_to_load_pdf),
                            onTryAgain = {
                                if (submittedLetter.fileUrl != null){
                                    action(DocPreviewApprovalUiAction.GetPDF(
                                        submittedLetter.title,
                                        submittedLetter.fileUrl
                                    ))
                                }
                            }
                        )

                }
                is Resource.Idle -> Unit
            }
        }

        // on rejecting
        if (isRejecting){
            DialogRejectingForm(
                onSubmit = { reason ->
                    action(
                        DocPreviewApprovalUiAction.Rejecting(
                            submittedLetter.id, reason
                        )
                    )
                    isRejecting = false
                },
                onDismiss = {isRejecting = false}
            )
        }

        // on signing
        if (isSigning){
            DialogSigningForm(
                onDismiss = {isSigning = false},
                onSubmit = { passphrase ->
                    action(DocPreviewApprovalUiAction.Signing(submittedLetter.id, passphrase))
                    isSigning = false
                }
            )
        }

        // signing result
        SigningResultView(
            signingResult = state.signingResult,
            onSuccess = { onNavigateBack(true) },
            onDismiss = { action(DocPreviewApprovalUiAction.ResetSigningResult) }
        )

    }
}


@Preview
@Composable
private fun SignedDocPreviewScreen_Preview(){
    KoinPreviewWrapper(modules = listOf(officerTaskModules)) {
        val viewModel: DocPreviewApprovalViewModel = koinViewModel()
        LaunchedEffect(key1 = Unit) {
            // Define the path where you want to create the file
            val filePath = "dummyFile.txt"

            // Define the content to be written to the file
            val fileContent = "This is a dummy file created programmatically in Kotlin."
            // Create a new file at the specified path
            val file = File(filePath)
            try {

                // Check if file already exists
                if (file.exists()) {
                    println("File already exists. Overwriting...")
                }

                // Write content to the file
                file.writeText(fileContent)

                println("File created and content written successfully.")
            } catch (e: IOException) {
                println("An error occurred while creating the file: ${e.message}")
            }

            viewModel.doAction(
                DocPreviewApprovalUiAction.SetDummyState(
                    state = DocPreviewApprovalUiState(
                        downloadFileResult = Resource.Success(file)
                    )
                )
            )
        }
        SILPUSITRONTheme {
            DocApprovalScreen(
                submittedLetter = SubmittedLetterDummies[0],
                onNavigateBack = {}
            )
        }
    }
}