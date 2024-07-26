package com.bruno13palhano.hqsmarvel.ui.common

import androidx.annotation.StringRes
import com.bruno13palhano.data.repository.utils.ErrorCode
import com.bruno13palhano.hqsmarvel.R

sealed class ErrorMessages(
    val code: Int,
    @StringRes val resourceId: Int
) {
    data object ItemNotFound : ErrorMessages(
        code = ErrorCode.HTTP_ITEM_NOT_FOUND,
        resourceId = R.string.item_not_found_error_message
    )

    data object OtherErrors : ErrorMessages(
        code = ErrorCode.OTHER_HTTP_ERRORS,
        resourceId = R.string.other_errors_message
    )

    data object NetworkError : ErrorMessages(
        code = ErrorCode.NETWORK_ERROR,
        resourceId = R.string.network_error_message
    )

    data object UnexpectedError : ErrorMessages(
        code = ErrorCode.UNEXPECTED_ERROR,
        resourceId = R.string.unexpected_error_message
    )
}