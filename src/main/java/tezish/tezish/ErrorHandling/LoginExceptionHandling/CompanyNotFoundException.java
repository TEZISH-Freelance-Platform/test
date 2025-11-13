package tezish.tezish.ErrorHandling.LoginExceptionHandling;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}