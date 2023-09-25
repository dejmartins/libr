package africa.semicolon.library.data.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
}
