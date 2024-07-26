package com.bruno13palhano.hqsmarvel.ui.common

sealed class UIState {
    data object Success : UIState()

    data object Loading : UIState()

    data class Error(val errorCode: Int) : UIState()
}