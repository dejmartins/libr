package africa.semicolon.library.data.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    private String emailAddress;
    private String password;
}
