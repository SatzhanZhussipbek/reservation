package jusan.reservation.controller;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.ReserveItem;
import jusan.reservation.exception.ErrorTemplate;
import jusan.reservation.exception.RoomBookedException;
import jusan.reservation.model.ClientDAO;
import jusan.reservation.repository.ClientRepository;
import jusan.reservation.repository.ReserveItemRepository;
import jusan.reservation.service.ClientService;
import jusan.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RoomService roomService;

    @Autowired
    ClientService clientService;

    @Autowired
    ReserveItemRepository reserveItemRepository;

    @GetMapping("/rooms")
    public ResponseEntity<Object> getRooms(@RequestBody ClientDAO clientDAO, Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(clientDAO.getId());
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Object> getRoom(@PathVariable long roomId, @RequestBody ClientDAO clientDAO, Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(clientDAO.getId());
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getRoom(roomId));
    }

    @PostMapping("/reservation/add")
    public ResponseEntity<Object> addReservation(@RequestParam long userId, @RequestBody ReserveItem reserveItem) {
        if (reserveItemRepository.existsById(reserveItem.getReservationId())) {
            if (reserveItemRepository.getReserveItemByReservationIdAndUserId(reserveItem.getReservationId(), userId).getPeriod()==reserveItem.getPeriod()) {
                throw new RoomBookedException("The room is already booked during this time period.");
            }
        }
        return ResponseEntity.ok(clientService.createReservation(reserveItem));
    }

    @DeleteMapping("/reservation/delete")
    public ResponseEntity<Object> deleteReservation(@RequestParam long reservationId, @RequestParam long userId,
                                                    @RequestBody ClientDAO clientDAO, Authentication authentication) {
        Client person = clientRepository.findClientById(clientDAO.getId());
        if (!authentication.getPrincipal().equals(person.getEmail()) ||
                !authentication.getCredentials().equals(person.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        clientService.deleteReservation(reservationId, userId);
        return new ResponseEntity<>(new ErrorTemplate(String.format("Reservation %d deleted", reservationId) ),
                HttpStatus.OK);
    }
}
