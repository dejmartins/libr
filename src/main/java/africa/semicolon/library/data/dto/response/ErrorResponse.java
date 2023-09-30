package africa.semicolon.library.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorResponse {
    private String error;
    private Object message;
    private String detail;
}
