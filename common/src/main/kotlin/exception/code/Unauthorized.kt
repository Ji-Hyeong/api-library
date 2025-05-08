package exception.code

import exception.ErrorSpec
import org.springframework.http.HttpStatus

enum class Unauthorized(
    override val message: String,
) : ErrorSpec {
    Default("인증되지 않은 사용자입니다."),
    InvalidToken("잘못된 토큰입니다."),
    ;

    override val status: HttpStatus get() = HttpStatus.UNAUTHORIZED
    override val code: String get() = this.name // Enum 상수의 이름을 코드로 사용
}
