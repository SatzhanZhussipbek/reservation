package jusan.reservation.exception;

public class RoomBookedException extends RuntimeException{

    public RoomBookedException(String id) {
        super(String.format("The room %s", id + " is already booked"));
    }

}
