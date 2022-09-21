package notulus.exception;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class InvalidLoginBodyException extends AuthenticationException {

    public InvalidLoginBodyException(String message) {
        super(message);
    }

}
