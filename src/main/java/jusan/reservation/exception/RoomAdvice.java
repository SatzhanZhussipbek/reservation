package jusan.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RoomAdvice {

    @ResponseBody
    @ExceptionHandler(RoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String roomNotFound(RoomNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RoomsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String roomsNotFound(RoomsNotFoundException ex) {
        return ex.getMessage();
    }


}
