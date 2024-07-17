package com.haltec.silpusitron.user.account.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.core.ui.component.SmallTopBar
import com.haltec.silpusitron.core.ui.theme.BackgroundLight

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier
){
    Column(
    ) {
        SmallTopBar(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.height(50.dp),
        ){
            Text(
                text = stringResource(R.string.account),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.secondary),
                colors = CardDefaults.cardColors().copy(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.5.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.weight(6f),
                        text = stringResource(R.string.profile),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }

            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.secondary),
                colors = CardDefaults.cardColors().copy(
                    containerColor = BackgroundLight
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.5.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.weight(6f),
                        text = stringResource(R.string.logout),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }
        }
    }
}