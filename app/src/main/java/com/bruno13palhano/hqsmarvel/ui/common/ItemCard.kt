package com.bruno13palhano.hqsmarvel.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ItemCard(
    modifier: Modifier,
    id: Long?,
    title: String?,
    thumbnail: String?,
    copyright: String?,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        thumbnail?.let { image ->
            AsyncImage(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(5)),
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .placeholderMemoryCacheKey(
                            key = "$title-$id"
                        )
                        .memoryCacheKey(
                            key = "$title-$id"
                        )
                        .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Text(
            modifier =
                Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .fillMaxWidth(),
            text = title ?: "",
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier =
                Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
            text = copyright ?: "",
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            fontStyle = FontStyle.Italic,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ItemCardPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ItemCard(
                modifier = Modifier.fillMaxSize(),
                id = 1L,
                title = "test",
                thumbnail = "",
                copyright = "© 2024 MARVEL",
                onClick = {}
            )
        }
    }
}