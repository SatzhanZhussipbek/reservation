package jusan.reservation.exception;

public class RoomNotFoundException extends RuntimeException{
        public RoomNotFoundException(String id) {
            super(String.format("Could not find invoice %s", id));
        }

}

