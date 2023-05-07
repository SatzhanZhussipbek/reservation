package jusan.reservation.exception;

public class ReservationsNotFoundException extends RuntimeException {
        public ReservationsNotFoundException(Long id) {
            super(String.format("Could not find reservations under this client id: %d", id));
        }
}

