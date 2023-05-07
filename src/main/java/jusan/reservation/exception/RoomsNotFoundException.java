package jusan.reservation.exception;

public class RoomsNotFoundException extends RuntimeException {
        public RoomsNotFoundException(Long id) {
            super(String.format("Could not find invoices under this client id: %d", id));
        }
}

