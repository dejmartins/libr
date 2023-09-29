package africa.semicolon.library.exception.registrationException;

import africa.semicolon.library.data.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(DuplicateEmailRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(DuplicateEmailRegistrationException ex){
        ErrorResponse response = ErrorResponse.builder()
                .error("Reg 0001")
                .message(ex.getMessage())
                .detail("Ensure that the email sent on request has not been registered with already")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
