package tezish.tezish.ErrorHandling;

import lombok.Data;

@Data
public class ApiError {
    private String path;
    private String error;
    private String message;
    private int status;

    public ApiError(String path, String error, String message, int status) {
        this.path = path;
        this.error = error;
        this.message = message;
        this.status = status;
    }
}

