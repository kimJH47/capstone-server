package capstone.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class ResponseStatusException extends RuntimeException{
    HttpStatus httpStatus;
    @Nullable
    String reason;
    @Nullable
    Throwable cause;


}
