package africa.semicolon.library.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessResponse {
    private Object data;
}
