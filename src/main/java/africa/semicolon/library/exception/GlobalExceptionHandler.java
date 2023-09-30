package africa.semicolon.library.exception;

import africa.semicolon.library.data.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleRegistrationException(Exception ex){
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .error("Unexpected Error")
//                .message("An unexpected error occurred. Please try again later")
//                .detail(ex.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Validation Error")
                .message(errors)
                .detail("Ensure all request details are valid")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
