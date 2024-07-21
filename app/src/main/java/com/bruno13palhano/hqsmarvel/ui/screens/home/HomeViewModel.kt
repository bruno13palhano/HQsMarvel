package com.bruno13palhano.hqsmarvel.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bruno13palhano.data.di.ComicsRep
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.repository.comics.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        @ComicsRep private val comicsRepository: ComicsRepository
    ) : ViewModel() {
        val comics = comicsRepository.getComics().cachedIn(viewModelScope)

        fun updateFavorite(comic: Comic) {
            viewModelScope.launch {
                comicsRepository.updateComicFavorite(
                    id = comic.comicId,
                    isFavorite = !comic.isFavorite
                )
            }
        }
    }