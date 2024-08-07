package com.silpusitron.feature.submission.form.ui.parts

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silpusitron.common.util.AllowedFileExtension
import com.silpusitron.common.util.FileHelper
import com.silpusitron.common.util.GetContentWithMultiFilter
import com.silpusitron.core.ui.component.InputLabel
import com.silpusitron.core.ui.parts.SubmitSuccessView
import com.silpusitron.core.ui.parts.getAppTextFieldColors
import com.silpusitron.core.ui.theme.SuccessColor
import com.silpusitron.feature.submission.R
import com.silpusitron.feature.submission.form.ui.SubmissionDocUiAction
import com.silpusitron.feature.submission.form.ui.SubmissionDocUiState
import com.silpusitron.shared.form.domain.model.FileAbsolutePath
import com.silpusitron.shared.form.domain.model.getFilename
import com.silpusitron.shared.form.domain.model.isRequiredFile
import com.silpusitron.shared.form.ui.components.FormTextField
import kotlinx.coroutines.launch
import kotlin.math.abs
import com.silpusitron.core.ui.R as CoreR


@Composable
fun FormRequirementDocs(
    modifier: Modifier = Modifier,
    state: SubmissionDocUiState,
    action: (SubmissionDocUiAction) -> Unit
) {
    if (state.requirementDocs.isEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubmitSuccessView(
                borderColor = Color.Unspecified,
                text = {
                    Text(
                        text = stringResource(id = R.string.doc_has_complete),
                        style = TextStyle.Default.copy(
                            color = SuccessColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            )
        }
    } else {

        val context = LocalContext.current

        Column(
            modifier = modifier
        ){
            Text(
                text = stringResource(id = R.string.requirement_docs),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val inputs = state.requirementDocs.values.toList()
            LazyColumn {
                items(
                    count = inputs.size,
                    key = {
                        inputs[it].inputName+inputs[it].isValid
                    }
                ) { it ->
                    val item = inputs[it]
                    var hasFocus by remember {
                        mutableStateOf(false)
                    }
                    var filename: String by remember {
                        mutableStateOf(item.getFilename())
                    }

                    FormTextField(
                        colors = getAppTextFieldColors().copy(
                            cursorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        readOnly = true,
                        value = filename,
                        onValueChange = {},
                        label = {
                            if (!hasFocus){
                                InputLabel(
                                    label = item.inputLabel,
                                    isRequired = item.isRequiredFile()
                                )
                            }
                        },
                        placeholder = {
                            if (hasFocus){
                                InputLabel(
                                    label = item.inputLabel,
                                    isRequired = item.isRequiredFile()
                                )
                            }
                        },
                        inputLabel = item.inputLabel,
                        singleLine = true,
                        inputData = item,
                        trailingIcon = {
                            FileInputActionButton(
                                absoluteFilePath = item.value,
                                onResult = { uri, newFilename ->
                                    filename = newFilename
                                    action(
                                        SubmissionDocUiAction.SetFileInput(
                                            context = context,
                                            inputKey = item.inputName.toInt(),
                                            value = uri
                                        )
                                    )
                                }
                            )
                        },
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth()
                            .onFocusChanged {
                                hasFocus = it.isFocused
                            }
                    )
                }

            }
        }
    }
}

@Composable
private fun FileInputActionButton(
    absoluteFilePath: FileAbsolutePath?,
    onResult: (uri: Uri?, filename: String) -> Unit
) {
    val context = LocalContext.current

    var fileUri: Uri? by remember {
        mutableStateOf(
            if (absoluteFilePath?.contains("http") == true) null else
                absoluteFilePath?.let { FileHelper.absolutePathToUri(context, it) }
        )
    }

    var mimeType: String? by remember {
        mutableStateOf(
            if (absoluteFilePath?.contains("http") == true) null else
            absoluteFilePath?.let { FileHelper.getMimeType(it) })
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = GetContentWithMultiFilter(),
        onResult = { uri ->
            fileUri = uri
            mimeType = uri?.let { FileHelper.getMimeType(context, it) }

            onResult(uri, uri?.let { FileHelper.getFileName(context, it) } ?: "")
        }
    )
    val scope = rememberCoroutineScope()

    Row {
        TextButton(
            modifier = Modifier.fillMaxHeight(),
            onClick = {
                filePickerLauncher.launch(AllowedFileExtension.extensions)
            }
        ) {
            Text(text = stringResource(id = CoreR.string.choose))
        }
        if (absoluteFilePath != null) {
            IconButton(
                modifier = Modifier.fillMaxHeight(),
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    if (fileUri != null && mimeType != null){
                        try {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(fileUri, mimeType)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant temporary read permission
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("FilePreview", e.localizedMessage ?: "file preview error")
                        }
                    }
                    else if (absoluteFilePath.contains("http")){
                        scope.launch {
                            try {
                                FileHelper.checkContentTypeAndOpen(context, absoluteFilePath)
                            }catch (e: Exception) {
                                Log.e("FilePreview absolutepath", e.localizedMessage ?: "file preview error")
                            }

                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Preview,
                    contentDescription = stringResource(id = CoreR.string.open)
                )
            }
        }
    }
}

