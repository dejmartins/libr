package africa.semicolon.library.exception.loginException;

import africa.semicolon.library.data.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(InvalidLoginDetailsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(InvalidLoginDetailsException ex){
        ErrorResponse response = ErrorResponse.builder()
                .error("Log 0001")
                .message(ex.getMessage())
                .detail("Ensure that the email and password are correct.")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshToken(InvalidTokenException ex){
        ErrorResponse response = ErrorResponse.builder()
                .error("Log 0002 - Invalid Grant")
                .message(ex.getMessage())
                .detail("Ensure that the token is correct and has not expired.")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
