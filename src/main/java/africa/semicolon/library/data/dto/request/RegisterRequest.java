package africa.semicolon.library.data.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterRequest {
    @Email(message = "Invalid Email Format")
    @NotBlank(message = "Invalid Email: Field is empty")
    private String emailAddress;
    @NotBlank(message = "Invalid Firstname: Field is empty")
    private String firstName;
    @NotBlank(message = "Invalid Lastname: Field is empty")
    private String lastName;
    @NotBlank(message = "Invalid Password: Field is empty")
    private String password;
}
