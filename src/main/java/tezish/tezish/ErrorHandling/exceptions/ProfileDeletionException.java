package tezish.tezish.ErrorHandling.exceptions;

public class ProfileDeletionException extends RuntimeException {
    private final int errorCode;
    private final String azerbaijaniMessage;

    public ProfileDeletionException(int errorCode, String message, String azerbaijaniMessage) {
        super(message);
        this.errorCode = errorCode;
        this.azerbaijaniMessage = azerbaijaniMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getAzerbaijaniMessage() {
        return azerbaijaniMessage;
    }
}



