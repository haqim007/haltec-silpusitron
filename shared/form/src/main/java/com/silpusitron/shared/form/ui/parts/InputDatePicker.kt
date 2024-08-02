package com.silpusitron.shared.form.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.silpusitron.common.util.toStringFormat
import com.haltec.silpusitron.core.ui.theme.DisabledInputContainer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import com.haltec.silpusitron.core.ui.R as CoreR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDatePicker(
    modifier: Modifier = Modifier,
    placeholder: @Composable() (() -> Unit)? = null,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors().copy(
        disabledContainerColor = DisabledInputContainer,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    ),
    value: LocalDateTime? = null,
    setDate: (LocalDateTime?) -> Unit,
    minDate: LocalDateTime? = null,
    maxDate: LocalDateTime? = null
){

    var timestamp: Long? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(key1 = value) {
        timestamp = value
            ?.toInstant(TimeZone.currentSystemDefault())
            ?.toEpochMilliseconds()
    }
    val minDateTimestamp = minDate
        ?.toInstant(TimeZone.currentSystemDefault())
        ?.toEpochMilliseconds()
    val maxDateTimestamp = maxDate
        ?.toInstant(TimeZone.currentSystemDefault())
        ?.toEpochMilliseconds()
    val date by remember(timestamp) {
        derivedStateOf {
            Instant.fromEpochMilliseconds(timestamp ?: 0L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    var openDialog by remember { mutableStateOf(false) }
    val dateString = date.toStringFormat()

    var showAlertMinDialog by remember {
        mutableStateOf(false)
    }
    var showAlertMaxDialog by remember {
        mutableStateOf(false)
    }

    Column {
        TextField(
            readOnly = true,
            placeholder = placeholder,
            modifier = modifier.fillMaxWidth(),
            value = if (timestamp == null) "" else dateString,
            colors = colors,
            onValueChange = {},
            trailingIcon = {
                Row {
                    if (timestamp != null){
                        IconButton(onClick = { timestamp = null }) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = stringResource(CoreR.string.cancel)
                            )
                        }
                    }
                    IconButton(onClick = { openDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        if (openDialog) {
            val pickerState = rememberDatePickerState(
                initialSelectedDateMillis = timestamp,
            )
            val confirmEnabled = remember {
                derivedStateOf { pickerState.selectedDateMillis != null }
            }
            DatePickerDialog(
                onDismissRequest = {
                    openDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                            if (
                                maxDateTimestamp != null &&
                                maxDateTimestamp > 0 &&
                                (pickerState.selectedDateMillis ?: 0) > maxDateTimestamp){
                                showAlertMaxDialog = true
                            }
                            else if(minDateTimestamp != null &&
                                minDateTimestamp > 0 &&
                                (pickerState.selectedDateMillis ?: 0) < minDateTimestamp){
                                showAlertMinDialog = true
                            }else{
                                timestamp = pickerState.selectedDateMillis
                                setDate(date)
                            }
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(stringResource(id = CoreR.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog = false }) {
                        Text(stringResource(id = CoreR.string.cancel))
                    }
                }
            ) {
                    DatePicker(state = pickerState)
            }
        }

        if (showAlertMinDialog){
            openDialog = false
            AlertDialog(
                onDismissRequest = {
                    showAlertMinDialog = false
                    openDialog = true
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showAlertMinDialog = false
                            openDialog = true
                        }
                    ) {
                        Text(stringResource(CoreR.string.ok))
                    }
                },
                icon = {
                    Icon(Icons.Default.Warning, contentDescription = null)
                },
                title = {
                    Text(text = stringResource(CoreR.string.attention_))
                },
                text = {
                    Text(
                        text = stringResource(
                            CoreR.string.cannot_less_than_,
                            minDate?.toStringFormat() ?: "-"
                        )
                    )
                }
            )
        }

        if (showAlertMaxDialog){
            openDialog = false
            AlertDialog(
                onDismissRequest = {
                    showAlertMaxDialog = false
                    openDialog = true
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showAlertMaxDialog = false
                            openDialog = true
                        }
                    ) {
                        Text(stringResource(CoreR.string.ok))
                    }
                },
                icon = {
                    Icon(Icons.Default.Warning, contentDescription = null)
                },
                title = {
                    Text(text = stringResource(CoreR.string.attention_))
                },
                text = {
                    Text(
                        text = stringResource(
                            CoreR.string.cannot_more_than_,
                            maxDate?.toStringFormat() ?: "-"
                        )
                    )
                }
            )
        }
    }
    
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDateRangePicker(
    modifier: Modifier = Modifier,
    placeholder: @Composable() (() -> Unit)? = null,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors().copy(
        disabledContainerColor = DisabledInputContainer,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    ),
    startDateTimestamp: Long?,
    endDateTimestamp: Long?,
    setStartDateTimestamp: (Long?) -> Unit,
    setEndDateTimestamp: (Long?) -> Unit,
){



    var openDialog by remember { mutableStateOf(false) }

    val startDate = Instant.fromEpochMilliseconds(startDateTimestamp ?: 0L)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val startDateString = "${startDate.year}-${startDate.monthNumber}-${startDate.dayOfMonth}"

    Column {
        TextField(
            placeholder = placeholder,
            modifier = modifier.fillMaxWidth(),
            value = if (startDateTimestamp == null) "" else startDateString,
            colors = colors,
            onValueChange = {},
            trailingIcon = {
                Row {
                    if (startDateTimestamp != null){
                        IconButton(onClick = {
                            setStartDateTimestamp(null)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = stringResource(CoreR.string.cancel)
                            )
                        }
                    }
                    IconButton(onClick = { openDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        val endDate = Instant.fromEpochMilliseconds(endDateTimestamp ?: 0L)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val endDateString = "${endDate.year}-${endDate.monthNumber}-${endDate.dayOfMonth}"
        TextField(
            placeholder = placeholder,
            modifier = modifier.fillMaxWidth(),
            value = if (endDateTimestamp == null) "" else endDateString,
            colors = colors,
            onValueChange = {},
            trailingIcon = {
                Row {
                    if (endDateTimestamp != null){
                        IconButton(onClick = {
                            setEndDateTimestamp(null)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = stringResource(CoreR.string.cancel)
                            )
                        }
                    }
                    IconButton(onClick = { openDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        if (openDialog) {
            val pickerState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = startDateTimestamp,
                initialSelectedEndDateMillis = endDateTimestamp
            )
            val confirmEnabled = remember {
                derivedStateOf {
                    pickerState.selectedStartDateMillis != null &&
                        pickerState.selectedEndDateMillis != null
                }
            }
            DatePickerDialog(
                onDismissRequest = {
                    openDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                            setStartDateTimestamp(pickerState.selectedStartDateMillis)
                            setEndDateTimestamp(pickerState.selectedEndDateMillis)
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(stringResource(id = CoreR.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog = false }) {
                        Text(stringResource(id = CoreR.string.cancel))
                    }
                }
            ) {
                DateRangePicker(state = pickerState)
            }
        }
    }

}