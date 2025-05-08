package exception.code

import exception.ErrorSpec
import org.springframework.http.HttpStatus

enum class BadRequest(
    override val message: String,
) : ErrorSpec {
    Default("잘못된 Request 형식 입니다."),
    InvalidQueryParameter("잘못된 Query Parameter 입니다."),
    InvalidInputValue("잘못된 Request 형식 입니다."),
    InvalidEnumValue("잘못된 Enum Value 입니다."),
    ;

    override val status: HttpStatus get() = HttpStatus.BAD_REQUEST
    override val code: String get() = this.name // Enum 상수의 이름을 코드로 사용
}
