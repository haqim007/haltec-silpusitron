package com.silpusitron.feature.dashboard.exposed.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.silpusitron.core.ui.theme.DisabledInputContainer
import com.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.silpusitron.data.mechanism.Resource
import com.silpusitron.feature.dashboard.R
import com.silpusitron.shared.form.domain.model.InputOptions
import com.silpusitron.shared.form.domain.model.getValue
import com.silpusitron.shared.form.ui.parts.InputDatePicker
import kotlinx.datetime.LocalDateTime
import com.silpusitron.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardFilterView(
    modifier: Modifier = Modifier,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors().copy(
        disabledContainerColor = DisabledInputContainer,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    ),
    districtId: String?,
    startDate: LocalDateTime?,
    endDate: LocalDateTime?,
    districtOptions: Resource<InputOptions>,
    reloadDistrict: () -> Unit,
    setFilter: (districtId: String?, startDate: LocalDateTime?, endDate: LocalDateTime?) -> Unit
){
    var expanded by remember {
        mutableStateOf(false)
    }

    var districtIdState by remember {
        mutableStateOf(
            TextFieldValue(
                text = districtOptions.data?.options?.firstOrNull { it.key == districtId }?.label ?: ""
            )
        )
    }

    LaunchedEffect(key1 = districtOptions.data?.options) {
        districtIdState = TextFieldValue(
            text = districtOptions.data?.options?.firstOrNull { it.value == districtId }?.label ?: ""
        )
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = districtIdState,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = {
                    Text(stringResource(id = R.string.district))
                },
                trailingIcon = {
                    when(districtOptions){
                        is Resource.Error -> {
                            IconButton(onClick = {
                                reloadDistrict()
                                expanded = false // prevent dropdown from opening
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(com.silpusitron.core.ui.R.string.click_to_reload_this_options)
                                )
                            }
                        }
                        is Resource.Loading -> {
                            CircularProgressIndicator(
                                color = DisabledInputContainer
                            )
                        }
                        else -> {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    }
                },
                colors = colors,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                districtOptions.data?.options?.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            districtIdState =
                                TextFieldValue(
                                    text = option.label,
                                    selection = TextRange(option.label.length),
                                )
                            //onValueChange(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        }

        var startDateState: LocalDateTime? by remember {
            mutableStateOf(startDate)
        }
        var endDateState: LocalDateTime? by remember {
            mutableStateOf(endDate)
        }

        InputDatePicker(
            value = startDateState,
            placeholder = {
                Text(text = stringResource(CoreR.string.start_date))
            },
            setDate = {
                startDateState = it
            },
            maxDate = endDateState
        )

        InputDatePicker(
            value = endDateState,
            placeholder = {
                Text(text = stringResource(CoreR.string.end_date))
            },
            setDate = {
                endDateState = it
            },
            minDate = startDateState
        )

        Button(
            modifier = Modifier
                .padding(top = 50.dp, bottom = 6.dp)
                .fillMaxWidth()
            ,
            onClick = {
                setFilter(
                    if (districtIdState.text.isEmpty()) null
                        else
                            districtOptions.data?.getValue(districtIdState.text),
                    startDateState, endDateState
                )
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(5.dp),
        ) {
            Text(
                text = stringResource(CoreR.string.filter),
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Preview
@Composable
fun DashboardFilterView_Preview(){
    SILPUSITRONTheme {
        DashboardFilterView(
            districtOptions = Resource.Idle(),
            reloadDistrict = {},
            setFilter = {_,_,_ ->},
            districtId = null,
            startDate = null,
            endDate = null
        )
    }
}