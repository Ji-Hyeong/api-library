package util

import dto.ApiFailResponse
import dto.ApiSuccessResponse
import exception.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity

object ResponseUtil {
    fun <T> successResponse(
        message: String,
        data: T,
    ): ResponseEntity<ApiSuccessResponse<T>> =
        ResponseEntity
            .status(HttpStatus.OK)
            .contentType(APPLICATION_JSON)
            .body(
                ApiSuccessResponse(
                    success = true,
                    message = message,
                    data = data,
                ),
            )

    fun <T> successResponse(message: String): ResponseEntity<ApiSuccessResponse<Unit>> =
        ResponseEntity
            .status(HttpStatus.OK)
            .contentType(APPLICATION_JSON)
            .body(
                ApiSuccessResponse(
                    success = true,
                    message = message,
                ),
            )

    fun failResponse(error: ApiException): ResponseEntity<ApiFailResponse> =
        ResponseEntity
            .status(error.httpStatus)
            .contentType(APPLICATION_JSON)
            .body(ApiFailResponse(error))
}
