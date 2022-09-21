package notulus.exception;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@StandardException
public class InvalidLoginBodyException extends AuthenticationException {

    public InvalidLoginBodyException(String message) {
        super(message);
    }

}
