package notulus.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoRoleFoundException extends Exception {

    public NoRoleFoundException(String message) {
        super(message);
    }

}
