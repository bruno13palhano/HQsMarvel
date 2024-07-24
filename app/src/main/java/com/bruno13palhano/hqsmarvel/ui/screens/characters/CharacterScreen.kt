package com.bruno13palhano.hqsmarvel.ui.screens.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.hqsmarvel.R

@Composable
fun CharacterRoute(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.getCharacter(id = id) }

    val character by viewModel.character.collectAsStateWithLifecycle()

    CharacterContent(
        character = character,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterContent(
    character: Character?,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = character?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            character?.let { ch ->
                AsyncImage(
                    modifier = Modifier.sizeIn(minHeight = 360.dp, maxHeight = 720.dp),
                    model =
                        ImageRequest.Builder(LocalContext.current)
                            .data(ch.thumbnail)
                            .memoryCacheKey(key = "character-${ch.id}")
                            .build(),
                    contentDescription = stringResource(id = R.string.image_label),
                    contentScale = ContentScale.Crop
                )

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
                    text = ch.name ?: "",
                    style = MaterialTheme.typography.titleMedium
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
                    text = ch.description ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}