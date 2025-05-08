package dto

import exception.ApiException

data class ApiFailResponse(
    val success: Boolean = false,
    val message: String,
    val code: String,
) {
    constructor(apiException: ApiException) : this(
        success = false,
        message = apiException.getErrorMessage(),
        code = apiException.errorCode,
    )
}
