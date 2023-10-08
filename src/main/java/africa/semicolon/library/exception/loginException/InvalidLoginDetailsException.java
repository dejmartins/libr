package africa.semicolon.library.exception.loginException;

public class InvalidLoginDetailsException extends RuntimeException {
    public InvalidLoginDetailsException(String invalidAccountDetails) {
        super(invalidAccountDetails);
    }
}
