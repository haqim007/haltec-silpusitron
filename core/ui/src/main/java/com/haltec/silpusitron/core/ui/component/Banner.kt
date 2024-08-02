package com.haltec.silpusitron.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.backgroundGradient
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme


@Composable
fun Banner(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .backgroundGradient()
        ){
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BannerPreview(){
    SILPUSITRONTheme {
        Banner {
            LottieLoader(
                jsonRaw = R.raw.lottie_loader,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}