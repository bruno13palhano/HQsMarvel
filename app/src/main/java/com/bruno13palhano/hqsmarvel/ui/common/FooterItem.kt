package com.bruno13palhano.hqsmarvel.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FooterItem(
    modifier: Modifier = Modifier,
    text: String
) {
    HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

    Text(
        modifier = modifier,
        text = text,
        maxLines = 1,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelSmall,
        fontStyle = FontStyle.Italic,
        overflow = TextOverflow.Ellipsis
    )
}