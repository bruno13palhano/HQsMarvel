package com.bruno13palhano.data.repository.utils

/**
 * Custom implementation to inform the users about the errors.
 *
 * @param code an integer to identify the error code.
 *
 * @param cause Error cause.
 */
class CodeInfoException(
    code: String,
    cause: Throwable?
) : Exception(code, cause)