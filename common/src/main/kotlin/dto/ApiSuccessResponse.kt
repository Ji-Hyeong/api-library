package dto

data class ApiSuccessResponse<T>(
    val success: Boolean = true,
    val message: String,
    val data: T? = null,
)
