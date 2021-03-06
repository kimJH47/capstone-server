package capstone.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;


@Getter
@AllArgsConstructor
public enum ErrorCode {


    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    CHALLENGE_FULL_USERS(BAD_REQUEST, "챌린지의 참가인원이 가득 찾습니다"),
    CHALLENGE_NOT_PUBLIC(BAD_REQUEST, "챌린지가 공개상태가 아닙니다"),
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    CHALLENGE_ROLE_TYPE_NOT_ADIMIN(BAD_REQUEST, "챌린지 변경권한이 없습니다"),

    MEMBER_NOT_FOUND(BAD_REQUEST, "해당 멤버는 존재하지 않습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
