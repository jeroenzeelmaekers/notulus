package notulus.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoUserFoundException extends Exception {

    public NoUserFoundException(String message) {
        super(message);
    }

}
