package africa.semicolon.library.exception;

import africa.semicolon.library.data.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleRegistrationException(Exception ex){
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .error("Auth 0001")
//                .message(ex.getMessage())
//                .detail("")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }
}
