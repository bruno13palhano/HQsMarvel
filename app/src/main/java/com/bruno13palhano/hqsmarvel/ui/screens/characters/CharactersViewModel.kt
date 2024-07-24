package com.bruno13palhano.hqsmarvel.ui.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bruno13palhano.data.di.CharacterSummaryRep
import com.bruno13palhano.data.model.CharacterSummary
import com.bruno13palhano.data.repository.charactersummary.CharacterSummaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel
    @Inject
    constructor(
        @CharacterSummaryRep private val characterSummaryRepository: CharacterSummaryRepository
    ) : ViewModel() {
        private val _characters = MutableStateFlow<PagingData<CharacterSummary>>(PagingData.empty())

        val characters =
            _characters
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = PagingData.empty()
                )

        fun getCharactersSummary(comicId: Long) {
            viewModelScope.launch {
                characterSummaryRepository.getCharactersSummary(comicId = comicId)
                    .cachedIn(viewModelScope)
                    .collect {
                        _characters.value = it
                    }
            }
        }
    }