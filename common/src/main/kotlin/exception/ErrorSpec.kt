package exception

import org.springframework.http.HttpStatus

// 모든 에러 정의가 구현할 인터페이스
interface ErrorSpec {
    val status: HttpStatus // 해당 에러의 HTTP 상태 코드
    val message: String // 기본 에러 메시지
    val code: String // 에러를 식별하는 코드 (enum의 이름 사용)
}
