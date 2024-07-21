package com.bruno13palhano.hqsmarvel.ui.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bruno13palhano.data.di.CharacterRep
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.repository.character.CharacterRepository
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
        @CharacterRep private val characterRepository: CharacterRepository
    ) : ViewModel() {
        private val _characters = MutableStateFlow<PagingData<Character>>(PagingData.empty())

        val characters =
            _characters
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = PagingData.empty()
                )

        fun fetchCharacters(id: Long) {
            viewModelScope.launch {
                characterRepository.getCharacters(id)
                    .cachedIn(viewModelScope)
                    .collect {
                        _characters.value = it
                    }
            }
        }
    }