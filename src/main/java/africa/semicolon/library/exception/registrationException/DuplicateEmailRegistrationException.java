package africa.semicolon.library.exception.registrationException;

public class DuplicateEmailRegistrationException extends RuntimeException{
    public DuplicateEmailRegistrationException(String message){
        super(message);
    }
}
