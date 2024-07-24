package com.bruno13palhano.hqsmarvel.ui.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.data.di.CharacterRep
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.repository.character.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    @CharacterRep private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _character = MutableStateFlow<Character?>(null)
    val character = _character
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = null
        )

    fun getCharacter(id: Long) {
        viewModelScope.launch {
            characterRepository.getCharacter(id = id)
                .catch { it.printStackTrace() }
                .collect {
                    _character.value = it
                }
        }
    }
}