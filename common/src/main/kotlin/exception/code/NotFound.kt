package exception.code

import exception.ErrorSpec
import org.springframework.http.HttpStatus

enum class NotFound(
    override val message: String,
) : ErrorSpec {
    Endpoint("해당 엔드포인트를 찾을 수 없습니다."),
    ;

    override val status: HttpStatus get() = HttpStatus.NOT_FOUND
    override val code: String get() = this.name // Enum 상수의 이름을 코드로 사용
}
