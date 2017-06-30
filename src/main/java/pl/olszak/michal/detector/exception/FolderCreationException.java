package pl.olszak.michal.detector.exception;

/**
 * @author molszak
 *         created on 30.06.2017.
 */
public class FolderCreationException extends Exception {

    public FolderCreationException() {
    }

    public FolderCreationException(String message) {
        super(message);
    }

    public FolderCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FolderCreationException(Throwable cause) {
        super(cause);
    }

    public FolderCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
