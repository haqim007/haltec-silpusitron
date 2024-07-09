package com.haltec.silpusitron.feature.dashboard.exposed.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.R as CoreR
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.theme.gradientColors
import com.haltec.silpusitron.data.di.dataModule
import com.haltec.silpusitron.feature.dashboard.common.di.dashboardModule
import com.haltec.silpusitron.feature.dashboard.common.ui.parts.DashboardContent
import org.koin.compose.KoinApplication


@Composable
fun DashboardExposedScreen(
    modifier: Modifier = Modifier,
    sharedModifier: Modifier = Modifier,
    state: DashboardExposedUiState,
    action: (action: DashboardExposedUiAction) -> Unit,
    onLogin: () -> Unit
){

    LaunchedEffect(key1 = Unit) {
        action(DashboardExposedUiAction.GetData)
    }

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        gradientColors,
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 1000f),
                    ),
                    shape = RoundedCornerShape(
                        bottomEnd = 20.dp, bottomStart = 20.dp
                    )
                )
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp)

        ){
            Image(
                painter = painterResource(id = CoreR.drawable.logo),
                contentDescription = "logo",
                modifier = sharedModifier
                    .height(40.dp)
            )

            Button(
                modifier = Modifier
                    .width(90.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp),
                onClick = onLogin
            ) {
                Text(
                    text = stringResource(id = CoreR.string.login),
                    style = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

        }

        DashboardContent(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 18.dp),
            data = state.data,
            onTryAgain = {action(DashboardExposedUiAction.GetData)}
        )


    }
}


@Preview(showBackground = true)
@Composable
private fun DashboardExposedScreenPreview(){
    KoinApplication(application = {
        modules(listOf(commonModule, dataModule, dashboardModule))
    }) {
        val state by remember {
            mutableStateOf(
                dashboardUiStateDummy
            )
        }
        SILPUSITRONTheme {
            DashboardExposedScreen(
                state = state,
                action = {action -> },
                onLogin = {}
            )
        }
    }
}
