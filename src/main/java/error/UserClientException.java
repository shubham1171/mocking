package error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserClientException extends RuntimeException {

    private final int httpStatus;

    private final String reason;

    private final ErrorResponse response;
}
