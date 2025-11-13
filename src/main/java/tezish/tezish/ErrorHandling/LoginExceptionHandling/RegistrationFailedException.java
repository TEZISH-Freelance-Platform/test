package tezish.tezish.ErrorHandling.LoginExceptionHandling;

public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message) {
        super(message);
    }
}