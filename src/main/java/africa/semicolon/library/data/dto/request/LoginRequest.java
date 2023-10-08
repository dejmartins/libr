package africa.semicolon.library.data.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class LoginRequest {

    @Email(message = "Invalid Email Format")
    @NotBlank(message = "Invalid Email: Field is empty")
    private String emailAddress;
    @NotBlank(message = "Invalid Password: Field is empty")
    private String password;
}
