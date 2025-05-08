package exception.code

import exception.ErrorSpec
import org.springframework.http.HttpStatus

enum class MethodNotAllowed(
    override val message: String,
) : ErrorSpec {
    Default("지원하지 않는 HTTP 메서드입니다."),
    ;

    override val status: HttpStatus get() = HttpStatus.METHOD_NOT_ALLOWED
    override val code: String get() = this.name // Enum 상수의 이름을 코드로 사용
}
