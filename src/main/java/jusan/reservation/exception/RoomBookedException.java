package jusan.reservation.exception;

public class RoomBookedException extends RuntimeException{

    public RoomBookedException(String id) {
        super(String.format("Could not find invoice %s", id));
    }

}
