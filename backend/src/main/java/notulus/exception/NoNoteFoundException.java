package notulus.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoNoteFoundException extends Exception {

    public NoNoteFoundException(String message) {
        super(message);
    }

}
