package com.haltec.silpusitron.feature.auth.login.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.common.di.commonModule
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.core.ui.parts.ContainerWithBanner
import com.haltec.silpusitron.feature.auth.common.domain.UserType
import com.haltec.silpusitron.feature.auth.di.authModule
import org.koin.compose.KoinApplication
import com.haltec.silpusitron.core.ui.R as CoreR


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    userType: UserType,
    onProfileDataComplete: () -> Unit,
    sharedModifier: Modifier = Modifier,
    onProfileDataIncomplete: () -> Unit
){

    ContainerWithBanner(
        modifier = modifier
            .fillMaxSize(),
        bannerModifier = Modifier
            .height(280.dp),
        sharedModifier = sharedModifier
    ) {
        LoginForm(
            modifier = Modifier
                .defaultMinSize(minHeight = 300.dp)
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 30.dp
                ),
            userType = userType,
            onProfileDataComplete = onProfileDataComplete,
            onProfileDataIncomplete = onProfileDataIncomplete
        )

        Image(
            painter = painterResource(id = CoreR.drawable.bse),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 32.dp)
                .width(160.dp)
                .height(80.dp)
        )

        Text(
            "Copyright SILPUSITORIN Kota Blitar",
            style = TextStyle.Default.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(top = 16.dp)
        )
    }

}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val context = LocalContext.current
    KoinApplication(application = {
        modules(listOf(commonModule, authModule))
    }) {
        SILPUSITRONTheme {
            LoginScreen(
                userType = UserType.APP,
                onProfileDataComplete = {
                    Toast
                        .makeText(
                            context,
                            "Succeeded",
                            Toast.LENGTH_LONG
                        )
                        .show()
                },
                onProfileDataIncomplete = {
                    Toast
                        .makeText(
                            context,
                            "Succeeded",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            )
        }
    }
}