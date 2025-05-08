package exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import dto.ApiFailResponse
import exception.code.BadRequest
import exception.code.InternalServerError
import exception.code.MethodNotAllowed
import exception.code.NotFound
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
import util.ResponseUtil

@RestControllerAdvice
class ControllerAdvice {
    private val logger = LoggerFactory.getLogger(ControllerAdvice::class.java)

    @Value("\${spring.profiles.active}")
    private lateinit var activeProfile: Array<String>

    @Value("\${server.name:unknown-server}")
    private lateinit var serverName: String

    /*
    최상위 Exception 및 ApiException 을 제외한 모든 Exception 을 처리한다.
     */
    @ExceptionHandler(Exception::class)
    fun unhandledException(
        e: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("UnhandledException: {} {} errMessage={}\n", request.method, request.requestURI, e.message)
        return ResponseUtil.failResponse(ApiException(InternalServerError.Default))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun noResourceFoundException(
        e: NoResourceFoundException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("NoResourceFoundException: {} {} errMessage={}\n", request.method, request.requestURI, e.message)
        return ResponseUtil.failResponse(ApiException(NotFound.Endpoint))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun catchHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("HttpRequestMethodNotSupportedException: {} {} errMessage={}\n", request.method, request.requestURI, e.message)
        return ResponseUtil.failResponse(ApiException(MethodNotAllowed.Default, e.message))
    }

    /*
        ApiException 을 처리한다.
     */
    @ExceptionHandler(ApiException::class)
    fun apiException(
        e: ApiException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("ApiException: {} {} errMessage={}\n", request.method, request.requestURI, e.message)
//      TODO() 500일 경우 별도 처리 필요 (ex : Slack 알림)
//        if (e.httpStatus.is5xxServerError || response.status >= 500) {
//
//        }
        return ResponseUtil.failResponse(e)
    }

    /*
        MethodArgumentNotValidException 을 처리한다. 주로 Request DTO 의 Validation 에러를 처리한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodValidArgumentException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("MethodArgumentNotValidException: {} {} errMessage={}\n", request.method, request.requestURI, e.message)
        return ResponseUtil.failResponse(
            ApiException(
                BadRequest.InvalidInputValue,
                e.bindingResult.fieldError?.defaultMessage,
            ),
        )
    }

    /*
        HttpMessageNotReadableException 을 처리한다. 주로 Request DTO 의 Enum Validation 에러를 처리한다.
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        e: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("HttpMessageNotReadableException: {} {} errMessage={}\n", request.method, request.requestURI, e.message)
        val errorTitle = BadRequest.Default
        if (e.message == null) {
            return ResponseUtil.failResponse(ApiException(errorTitle))
        }
        val errorMessage =
            when (val cause = e.cause) {
                is MismatchedInputException -> "${cause.path.joinToString { it.fieldName }} 은(는) 필수값입니다."
                else -> errorTitle.message
            }
        return if (e.message!!.contains("Enum class")) {
            return ResponseUtil.failResponse(ApiException(BadRequest.InvalidEnumValue))
        } else {
            ResponseUtil.failResponse(ApiException(errorTitle, errorMessage))
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun methodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<ApiFailResponse> {
        logger.error("MethodArgumentTypeMismatchException: {} {} errMessage= {}\n", request.method, request.requestURI, e.message)
        return ResponseUtil.failResponse(ApiException(BadRequest.InvalidQueryParameter))
    }
}
