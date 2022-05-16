package capstone.server.exception;

import lombok.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                             .body(ErrorResponse.builder()
                                                .message(errorCode.getDetail())
                                                .code(errorCode.name())
                                                .build());
    }
}
