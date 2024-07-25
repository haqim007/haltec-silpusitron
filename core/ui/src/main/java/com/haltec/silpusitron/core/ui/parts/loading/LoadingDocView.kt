package com.haltec.silpusitron.core.ui.parts.loading

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.LottieLoader

@Composable
fun LoadingDocView(
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        LoadingView(
            loader = {
                LottieLoader(
                    jsonRaw = R.raw.lottie_loading_doc,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .size(300.dp)
                )
            }
        )
    }
}