package com.haltec.silpusitron.feature.dashboard.exposed.ui.parts

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.haltec.silpusitron.core.ui.R
import com.haltec.silpusitron.feature.dashboard.exposed.domain.model.NewsImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewsImagesPager(
    onDismissRequest: () -> Unit,
    data: List<NewsImage>,
) {

    var runSlides by remember {
        mutableStateOf(true)
    }

    val pagerState = rememberPagerState(
        pageCount = data.size
    )
    LaunchedEffect(key1 = Unit, key2 = pagerState.pageCount) {
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
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = {
                        onDismissRequest()
                        runSlides = false
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = stringResource(id = R.string.cancel)
                    )
                }


                HorizontalPager(state = pagerState) { page ->
                    // Our page content
                    Column {
                        Text(
                            text = data[page].title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .align(Alignment.CenterHorizontally)

                        )
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
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

            }
        }
    }
}