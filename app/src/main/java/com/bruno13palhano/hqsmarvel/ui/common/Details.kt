package com.bruno13palhano.hqsmarvel.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.hqsmarvel.R

@Composable
fun Details(
    title: String?,
    description: String?,
    thumbnail: String?,
    enableDelete: Boolean = false,
    onDeleteItemClick: () -> Unit = {}
) {
    ElevatedCard(
        modifier =
            Modifier
                .semantics { contentDescription = "Details" }
                .padding(vertical = 4.dp)
    ) {
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier =
                        Modifier
                            .padding(8.dp)
                            .size(144.dp)
                            .clip(RoundedCornerShape(5)),
                    painter = rememberAsyncImagePainter(model = thumbnail),
                    contentDescription = stringResource(id = R.string.image_label),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier =
                            Modifier
                                .padding(
                                    start = 8.dp,
                                    top = 8.dp,
                                    end = 8.dp,
                                    bottom = 4.dp
                                )
                                .fillMaxWidth(),
                        text = title ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier =
                            Modifier
                                .padding(
                                    start = 8.dp,
                                    top = 4.dp,
                                    end = 8.dp,
                                    bottom = 8.dp
                                )
                                .fillMaxWidth(),
                        text = description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (enableDelete) {
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onDeleteItemClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.remove_label)
                    )
                }
            }
        }
    }
}