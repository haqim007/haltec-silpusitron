package com.haltec.silpusitron.feature.requirementdocs.submission.ui.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.theme.BackgroundLight
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme
import com.haltec.silpusitron.feature.requirementdocs.common.domain.RequirementDoc
import com.haltec.silpusitron.feature.requirementdocs.common.domain.requirementDocDummies
import com.haltec.silpusitron.core.ui.R as CoreR

@Composable
fun ReqDocView(
    data: RequirementDoc,
    modifier : Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors().copy(
            containerColor = BackgroundLight
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp),
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f)
                    .padding(end = 16.dp),
                painter = painterResource(id = CoreR.drawable.doc_circle_blue),
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(4f)
            ) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Text(text = "Level Surat : ${data.letterLevel}")
                Text(text = "Jenis Surat : ${data.letterType}")
            }
        }
    }
}

@Preview
@Composable
fun ReqDocView_Preview(){
    SILPUSITRONTheme {
        ReqDocView(
            data = requirementDocDummies[0]
        )
    }
}