package africa.semicolon.library.data.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String password;
}
