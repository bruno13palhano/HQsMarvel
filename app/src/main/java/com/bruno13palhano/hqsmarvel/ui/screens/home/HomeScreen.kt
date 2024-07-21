package com.bruno13palhano.hqsmarvel.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.hqsmarvel.R
import com.bruno13palhano.hqsmarvel.ui.common.CircularProgress
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    onItemClick: (id: Long) -> Unit,
    showBottomMenu: (show: Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val comics = viewModel.comics.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var currentMessage by remember { mutableStateOf("") }
    val refreshLabel = stringResource(id = R.string.refresh_error_label)

    HomeContent(
        comics = comics,
        snackbarHostState = snackbarHostState,
        onItemClick = onItemClick,
        onFavoriteClick = viewModel::updateFavorite,
        showSnackbar = { message, retry ->
            if (currentMessage == message) return@HomeContent

            currentMessage = message

            coroutineScope.launch {
                val action =
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = refreshLabel,
                        duration = SnackbarDuration.Short,
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
        },
        showBottomMenu = showBottomMenu
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    comics: LazyPagingItems<Comic>,
    snackbarHostState: SnackbarHostState,
    onItemClick: (id: Long) -> Unit,
    onFavoriteClick: (comic: Comic) -> Unit,
    showSnackbar: (message: String, retry: () -> Unit) -> Unit,
    showBottomMenu: (show: Boolean) -> Unit
) {
    val messages =
        listOf(
            stringResource(id = R.string.refresh_error_label),
            stringResource(id = R.string.append_error_label),
            stringResource(id = R.string.no_comics_label)
        )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        }
    ) {
        var showCircularProgress by remember { mutableStateOf(false) }
        var selectedComic by remember { mutableStateOf<Comic?>(null) }
        val loadState = comics.loadState.mediator

        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier =
                    Modifier
                        .semantics { contentDescription = "List of comics" }
                        .padding(it),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
            ) {
                items(
                    count = comics.itemCount,
                    key = comics.itemKey { it.comicId }
                ) { index ->
                    comics[index]?.let { comic ->
                        AnimatedVisibility(
                            visible = comics[index] != selectedComic,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut(),
                            modifier = Modifier.animateItem()
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .sharedBounds(
                                            sharedContentState =
                                                rememberSharedContentState(
                                                    key = "comic-${comic.comicId}-bounds"
                                                ),
                                            animatedVisibilityScope = this@AnimatedVisibility,
                                            clipInOverlayDuringTransition =
                                                OverlayClip(
                                                    RoundedCornerShape(5)
                                                )
                                        )
                                        .fillMaxSize()
                            ) {
                                ElevatedCard(
                                    modifier =
                                        Modifier
                                            .padding(4.dp)
                                            .height(200.dp),
                                    onClick = {
                                        selectedComic = comic
                                        showBottomMenu(false)
                                    }
                                ) {
                                    Column {
                                        AsyncImage(
                                            modifier =
                                                Modifier
                                                    .padding(8.dp)
                                                    .sizeIn(maxHeight = 128.dp, minHeight = 128.dp)
                                                    .fillMaxWidth()
                                                    .align(Alignment.CenterHorizontally)
                                                    .clip(RoundedCornerShape(5)),
                                            model =
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(comic.thumbnail)
                                                    .crossfade(true)
                                                    .placeholderMemoryCacheKey(
                                                        key = "comic-${comic.comicId}"
                                                    )
                                                    .memoryCacheKey(
                                                        key = "comic-${comic.comicId}"
                                                    )
                                                    .build(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop
                                        )

                                        Text(
                                            modifier =
                                                Modifier.padding(
                                                    start = 16.dp,
                                                    top = 16.dp,
                                                    end = 16.dp
                                                ),
                                            text = comics[index]?.title ?: "",
                                            maxLines = 1,
                                            style = MaterialTheme.typography.bodyMedium,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                comics.apply {
                    when {
                        loadState?.refresh is LoadState.Loading -> {
                            item { showCircularProgress = true }
                        }

                        loadState?.refresh is LoadState.Error -> {
                            showCircularProgress = false
                            showSnackbar(messages[0]) { retry() }
                        }

                        loadState?.append is LoadState.Loading -> {
                            item { showCircularProgress = true }
                        }

                        loadState?.append is LoadState.Error -> {
                            showCircularProgress = false
                            showSnackbar(messages[1]) { retry() }
                        }

                        loadState?.append?.endOfPaginationReached ?: false -> {
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

            ComicsDetailsScreen(
                comic = selectedComic,
                onFavoriteClick = {
                    selectedComic = null
                    onFavoriteClick(it)
                    showBottomMenu(true)
                },
                onItemClick = {
                    selectedComic = null
                    onItemClick(it)
                    showBottomMenu(true)
                },
                onBack = {
                    showBottomMenu(true)
                    selectedComic = null
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ComicsDetailsScreen(
    modifier: Modifier = Modifier,
    comic: Comic?,
    onItemClick: (id: Long) -> Unit,
    onFavoriteClick: (comic: Comic) -> Unit,
    onBack: () -> Unit
) {
    AnimatedContent(
        targetState = comic,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "details-transition"
    ) { targetComic ->
        BackHandler { onBack() }

        Box(
            modifier =
                modifier
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(5))
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            if (targetComic != null) {
                Column(
                    modifier =
                        Modifier
                            .sharedBounds(
                                sharedContentState =
                                    rememberSharedContentState(
                                        key = "comic-${targetComic.comicId}-bounds"
                                    ),
                                animatedVisibilityScope = this@AnimatedContent,
                                clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(5))
                            )
                            .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        modifier =
                            Modifier
                                .sharedElement(
                                    rememberSharedContentState(
                                        key = "comic-${targetComic.comicId}"
                                    ),
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5, 5, 0, 0)),
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(targetComic.thumbnail)
                                .crossfade(true)
                                .placeholderMemoryCacheKey(
                                    key = "comic-${targetComic.comicId}"
                                )
                                .memoryCacheKey(key = "comic-${targetComic.comicId}")
                                .build(),
                        contentDescription = stringResource(id = R.string.image_label),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = targetComic.title ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = targetComic.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (!targetComic.isFavorite) {
                            Button(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(.5f)
                                        .padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 8.dp),
                                colors =
                                    ButtonDefaults.buttonColors().copy(
                                        containerColor =
                                            MaterialTheme.colorScheme.primary.copy(alpha = .5f)
                                    ),
                                onClick = { onFavoriteClick(targetComic) }
                            ) {
                                Icon(
                                    modifier = Modifier.padding(4.dp),
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = stringResource(id = R.string.add_favorite_label)
                                )
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = stringResource(id = R.string.favorite_label)
                                )
                            }
                        } else {
                            Button(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(.5f)
                                        .padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 8.dp),
                                colors =
                                    ButtonDefaults.buttonColors().copy(
                                        containerColor =
                                            MaterialTheme.colorScheme.primary.copy(alpha = 1f)
                                    ),
                                onClick = { onFavoriteClick(targetComic) }
                            ) {
                                Icon(
                                    modifier = Modifier.padding(4.dp),
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(id = R.string.remove_label)
                                )
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = stringResource(id = R.string.favorite_label)
                                )
                            }
                        }

                        ElevatedButton(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                            onClick = { onItemClick(targetComic.comicId) }
                        ) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.see_characters_label)
                            )
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = stringResource(id = R.string.characters_label)
                            )
                        }
                    }
                }
            }
        }
    }
}