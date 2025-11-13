package tezish.tezish.ErrorHandling.LoginExceptionHandling;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
