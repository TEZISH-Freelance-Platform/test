package tezish.tezish.ErrorHandling.exceptions;

import lombok.Getter;

@Getter
public class ShiftAuthorizationException extends RuntimeException {
    private final int errorCode;

    public ShiftAuthorizationException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static final int SHIFT_NOT_BELONG_TO_COMPANY = 427;
    public static final int MANAGER_NOT_ASSIGNED = 428;
    public static final int NO_PERMISSION = 429;
} 