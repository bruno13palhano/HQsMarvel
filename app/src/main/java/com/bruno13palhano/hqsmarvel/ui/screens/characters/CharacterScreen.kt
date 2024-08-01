package com.bruno13palhano.hqsmarvel.ui.screens.characters

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.repository.utils.ErrorCode
import com.bruno13palhano.hqsmarvel.R
import com.bruno13palhano.hqsmarvel.ui.common.CircularProgress
import com.bruno13palhano.hqsmarvel.ui.common.ErrorMessages
import com.bruno13palhano.hqsmarvel.ui.common.UIState

@Composable
fun CharacterRoute(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.getCharacter(id = id) }

    val character by viewModel.character.collectAsStateWithLifecycle()
    val uiState by viewModel.characterState.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var message by remember { mutableStateOf("") }
    val tryAgain = stringResource(id = R.string.try_again_label)

    when (uiState) {
        UIState.Success -> {
            showContent = true
        }

        UIState.Error(ErrorCode.HTTP_ITEM_NOT_FOUND) -> {
            message = stringResource(id = ErrorMessages.ItemNotFound.resourceId)

            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }

            showContent = true
        }

        UIState.Error(ErrorCode.OTHER_HTTP_ERRORS) -> {
            message = stringResource(id = ErrorMessages.OtherErrors.resourceId)

            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }

            showContent = true
        }

        UIState.Error(ErrorCode.NETWORK_ERROR) -> {
            message = stringResource(id = ErrorMessages.NetworkError.resourceId)

            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = tryAgain,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
                )
            }

            showContent = true
        }

        UIState.Error(ErrorCode.UNEXPECTED_ERROR) -> {
            message = stringResource(id = ErrorMessages.UnexpectedError.resourceId)

            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }

            showContent = true
        }

        UIState.Loading -> {
            showContent = false
        }

        else -> {
            showContent = true
        }
    }

    AnimatedContent(targetState = showContent, label = "character_content_state") { state ->
        if (state) {
            CharacterContent(
                character = character,
                snackbarHostState = snackbarHostState,
                navigateBack = navigateBack
            )
        } else {
            CircularProgress()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterContent(
    character: Character?,
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    .semantics { contentDescription = "Character details" }
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            character?.let { ch ->
                ch.thumbnail?.let { image ->
                    AsyncImage(
                        modifier = Modifier.sizeIn(minHeight = 360.dp, maxHeight = 720.dp),
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(image)
                                .memoryCacheKey(key = "character-${ch.id}")
                                .build(),
                        contentDescription = stringResource(id = R.string.image_label),
                        contentScale = ContentScale.Crop
                    )
                }

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