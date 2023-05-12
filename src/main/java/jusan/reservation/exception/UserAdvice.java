package jusan.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserAdvice {
    @ResponseBody
    @ExceptionHandler(UserIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userIdNotFound(UserIdNotFoundException ex) {
        return ex.getMessage();
    }
}
