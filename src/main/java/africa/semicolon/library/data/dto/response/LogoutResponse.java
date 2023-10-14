package africa.semicolon.library.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LogoutResponse {

    private boolean logout;
}
