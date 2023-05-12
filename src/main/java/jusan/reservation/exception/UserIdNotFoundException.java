package jusan.reservation.exception;

public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(String id) {
        super(String.format("Could not find user under the %s", id));
    }

}
