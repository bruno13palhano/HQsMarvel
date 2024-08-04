package com.bruno13palhano.hqsmarvel.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.ComicsRep
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.repository.comics.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteComicsViewModel
    @Inject
    constructor(
        @ComicsRep private val comicsRepository: ComicsRepository
    ) : ViewModel() {
        val favoriteComics =
            comicsRepository
                .getFavoriteComics()
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5000),
                    initialValue = emptyList()
                )

        fun deleteFavorite(comic: Comic) {
            viewModelScope.launch {
                comicsRepository.updateComicFavorite(id = comic.id, isFavorite = !comic.isFavorite)
            }
        }
    }