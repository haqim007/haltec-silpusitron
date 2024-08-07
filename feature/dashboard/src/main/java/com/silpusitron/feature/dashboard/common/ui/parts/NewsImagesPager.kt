package com.silpusitron.feature.dashboard.common.ui.parts

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.silpusitron.core.ui.R
import com.silpusitron.feature.dashboard.common.domain.model.NewsImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun NewsImagesPager(
    onDismissRequest: () -> Unit,
    data: List<NewsImage>,
) {


    var runSlides by remember {
        mutableStateOf(true)
    }

    val pagerState = rememberPagerState(
        pageCount = {data.size}
    )

    LaunchedEffect(key1 = Unit) {
        // dismiss immediately if data is emoty
        if (data.isEmpty()) {
            onDismissRequest()
        }
    }

    LaunchedEffect(key1 = pagerState.pageCount) {
        while (runSlides) {
            if (pagerState.pageCount == 0) {
                runSlides = false
                break
            } // break if pageCount reset to 0
            pagerState.animateScrollToPage(
                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    pagerState.currentPage + 1
                } else {
                    0
                }
            )

            // Introduce a delay and check if the coroutine is still active
            delay(5000) // Adjust the delay as needed
            if (!isActive) {
                runSlides = false
                break
            } // Exit the loop if the coroutine is cancelled
        }
    }

    Dialog(
        onDismissRequest = {
            onDismissRequest()
            runSlides = false
        }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(600.dp)
        ){
            // Draw a rectangle shape with rounded corners inside the dialog
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .padding(vertical = 50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = pagerState
                ) { page ->
                    // Our page content
                    Column(
                        Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = data[page].title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .align(Alignment.CenterHorizontally)

                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(data[page].imageURL)
                                    .crossfade(true)
                                    .build(),
                                placeholder = null,
                                error = painterResource(id = R.drawable.error_alert),
                                onError = {
                                    Log.e("NewsImage", it.result.throwable.message.toString())
                                },
                                contentDescription = data[page].title,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = {
                    onDismissRequest()
                    runSlides = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors().copy(
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = stringResource(id = R.string.cancel)
                )
            }
        }
    }
}