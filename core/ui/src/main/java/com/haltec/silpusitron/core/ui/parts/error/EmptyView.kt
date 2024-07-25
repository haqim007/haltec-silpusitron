package com.haltec.silpusitron.core.ui.parts.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.LottieLoader
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme


@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    message: String?,
    onTryAgain: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieLoader(
            jsonRaw = R.raw.lottie_empty,
            modifier = Modifier
                .padding(bottom = 14.dp)
                .size(250.dp)
        )

        Text(
            text = stringResource(R.string.oops_empty),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        Text(
            text = message ?: stringResource(R.string.unknown_error),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Button(
            modifier = Modifier
                .padding(top = 25.dp, bottom = 6.dp)
                .wrapContentWidth(),
            onClick = { onTryAgain() },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(5.dp),
        ) {
            Text(
                text = stringResource(R.string.try_again),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
private fun ErrorView_Preview(){
    SILPUSITRONTheme {
        EmptyView(message = "Error nihh") {}
    }
}