package tezish.tezish.ErrorHandling.LoginExceptionHandling;

public class TooManyFailedAttemptsException extends RuntimeException {
    public TooManyFailedAttemptsException(String message) {
        super(message);
    }
}
