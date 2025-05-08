package exception.code

import exception.ErrorSpec
import org.springframework.http.HttpStatus

enum class InternalServerError(
    override val message: String,
) : ErrorSpec {
    Default("알 수 없는 에러가 발생했습니다."),
    ;

    override val status: HttpStatus get() = HttpStatus.INTERNAL_SERVER_ERROR
    override val code: String get() = this.name // Enum 상수의 이름을 코드로 사용
}
