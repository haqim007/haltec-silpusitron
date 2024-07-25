package com.haltec.silpusitron.core.ui.parts.tab

import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme

@Composable
fun TabBarBadgeView(
    count: Int?
){
    count?.let {
        Badge {
            Text(count.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabBarBadgeViewPreview(){
    SILPUSITRONTheme {
        TabBarBadgeView(3)
    }
}