package com.bruno13palhano.hqsmarvel.ui.screens.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.hqsmarvel.R
import com.bruno13palhano.hqsmarvel.ui.common.Details

@Composable
fun FavoriteComicsRoute(viewModel: FavoriteComicsViewModel = hiltViewModel()) {
    val favoriteComics by viewModel.favoriteComics.collectAsStateWithLifecycle()

    FavoriteComicsContent(
        favoriteComics = favoriteComics,
        onDeleteItemClick = viewModel::deleteFavorite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteComicsContent(
    favoriteComics: List<Comic>,
    onDeleteItemClick: (comic: Comic) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.favorite_comics_label)) }
            )
        }
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .padding(it)
                    .semantics { contentDescription = "List of favorite comics" },
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = favoriteComics, key = { item -> item.comicId }) { comic ->
                Details(
                    title = comic.title,
                    description = comic.description,
                    thumbnail = comic.thumbnail,
                    enableDelete = true,
                    onDeleteItemClick = { onDeleteItemClick(comic) }
                )
            }
        }
    }
}