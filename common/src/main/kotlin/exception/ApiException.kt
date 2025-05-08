package exception

import org.springframework.http.HttpStatus

open class ApiException(
    val errorSpec: ErrorSpec,
    override val message: String? = null, // 메시지 처리
) : RuntimeException(errorSpec.message) { // 메시지 처리

    val httpStatus: HttpStatus
        get() = errorSpec.status

    val errorCode: String
        get() = errorSpec.code

    fun getErrorMessage(): String = message ?: errorSpec.message
}
