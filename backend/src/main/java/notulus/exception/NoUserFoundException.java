package notulus.exception;

import lombok.experimental.StandardException;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@StandardException
public class NoUserFoundException extends Exception {

    public NoUserFoundException(String message) {
        super(message);
    }

}
