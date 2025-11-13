package tezish.tezish.ErrorHandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class JobFinishedException extends RuntimeException {
    public JobFinishedException(String message) {
        super(message);
    }
}

