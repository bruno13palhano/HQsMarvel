package com.bruno13palhano.hqsmarvel.ui.screens.characters

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.hqsmarvel.R
import com.bruno13palhano.hqsmarvel.ui.common.CircularProgress
import kotlinx.coroutines.launch

@Composable
fun CharactersRoute(
    comicId: Long,
    onItemClick: (id: Long) -> Unit,
    navigateBack: () -> Unit,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = comicId) {
        viewModel.getCharactersSummary(comicId = comicId)
    }

    val characters = viewModel.characters.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var currentMessage by remember { mutableStateOf("") }
    val refreshLabel = stringResource(id = R.string.refresh_label)

    CharactersContent(
        characters = characters,
        snackbarHostState = snackbarHostState,
        navigateBack = navigateBack,
        onItemClick = onItemClick,
        showSnackbar = { message, retry ->
            if (currentMessage == message) return@CharactersContent

            currentMessage = message

            coroutineScope.launch {
                val action =
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = refreshLabel,
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )
                when (action) {
                    SnackbarResult.ActionPerformed -> {
                        currentMessage = ""
                        retry()
                    }

                    else -> {
                        return@launch
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersContent(
    characters: LazyPagingItems<CharacterSummary>,
    snackbarHostState: SnackbarHostState,
    onItemClick: (id: Long) -> Unit,
    showSnackbar: (message: String, retry: () -> Unit) -> Unit,
    navigateBack: () -> Unit
) {
    val messages =
        listOf(
            stringResource(id = R.string.refresh_error_label),
            stringResource(id = R.string.append_error_label),
            stringResource(id = R.string.no_characters_label)
        )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.characters_label)) },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics { contentDescription = "Navigate back" },
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back_label)
                        )
                    }
                }
            )
        }
    ) {
        var showCircularProgress by remember { mutableStateOf(false) }

        LazyColumn(
            modifier =
                Modifier
                    .semantics { contentDescription = "List of characters" }
                    .padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
        ) {
            items(count = characters.itemCount) { index ->
                characters[index]?.let { character ->
                    ListItem(
                        headlineContent = {
                            ElevatedCard(onClick = { onItemClick(character.id) }) {
                                ListItem(
                                    headlineContent = {
                                        Text(text = "${character.name}")
                                    }
                                )
                            }
                        }
                    )
                }
            }

            characters.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { showCircularProgress = true }
                    }

                    loadState.refresh is LoadState.Error -> {
                        showCircularProgress = false
                        showSnackbar(messages[0]) { retry() }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { showCircularProgress = true }
                    }

                    loadState.append is LoadState.Error -> {
                        showCircularProgress = false
                        showSnackbar(messages[1]) { retry() }
                    }

                    loadState.append.endOfPaginationReached -> {
                        showCircularProgress = false
                        showSnackbar(messages[2]) {}
                    }

                    else -> {
                        showCircularProgress = false
                    }
                }
            }
        }

        if (showCircularProgress) {
            CircularProgress()
        }
    }
}