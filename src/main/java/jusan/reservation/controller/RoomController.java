package jusan.reservation.controller;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.ReserveItem;
import jusan.reservation.entity.Room;
import jusan.reservation.exception.ErrorTemplate;
import jusan.reservation.exception.RoomBookedException;
import jusan.reservation.repository.ClientRepository;
import jusan.reservation.repository.ReserveItemRepository;
import jusan.reservation.repository.RoomRepository;
import jusan.reservation.service.ClientService;
import jusan.reservation.service.MailService;
import jusan.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {
    private ClientRepository clientRepository;
    private RoomService roomService;

    private ClientService clientService;

    private ReserveItemRepository reserveItemRepository;


    private RoomRepository roomRepository;
    @Autowired
    public RoomController(ClientRepository clientRepository, RoomService roomService,
                          ClientService clientService, ReserveItemRepository reserveItemRepository,
                          MailService mailService, RoomRepository roomRepository) {
        this.clientRepository = clientRepository;
        this.roomService = roomService;
        this.clientService = clientService;
        this.reserveItemRepository = reserveItemRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/rooms")
    public ResponseEntity<Object> getRooms(@RequestParam long userId, Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/rooms/filter/id")
    public ResponseEntity<Object> getFilteredRoomsById(@RequestParam long userId,
                                                   Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getFilteredRoomsById());
    }

    @GetMapping("/rooms/filter/floor")
    public ResponseEntity<Object> getFilteredRoomsByFloor(@RequestParam long userId,
                                                   Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getFilteredRoomsByFloor());
    }

    @GetMapping("/rooms/filter/capacity")
    public ResponseEntity<Object> getFilteredRoomsByCapacity(@RequestParam long userId,
                                                          Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getFilteredRoomsByCapacity());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Object> getRoom(@PathVariable long roomId, @RequestParam long userId, Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getRoom(roomId));
    }

    @GetMapping("/room/images/{roomId}")
    public ResponseEntity<Object> getImagesForRoom(@PathVariable long roomId, @RequestParam long userId, Authentication authentication) {
        Client searchPerson = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(searchPerson.getEmail()) ||
                !authentication.getCredentials().equals(searchPerson.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        return ResponseEntity.ok(roomService.getListOfImagesByRoomId(roomId));
    }
    // Cases for reservations:
    // Case a):
    //          2 ..... 3
    //       1 . .......  4
    // Case b):
    //          2 .. ...3
    //          2 ......3
    // Case c):
    //          2 ..... 3
    //          2 ..... .... 4
    // Case d):
    //          2 ..... 3
    //       1 ....... .3
    // Case e):
    //          2 ..... ... .. 4
    //            2.5 ..3
    // Case f):
    //          2 ......3
    //       1.......2.5
    // Case g):
    //          2.......3
    //            2.5......3.5
    @PostMapping("/reservation/add")
    public ResponseEntity<Object> addReservation(@RequestParam long roomId, @RequestBody ReserveItem reserveItem) {
        for (ReserveItem r : reserveItemRepository.getReserveItemsByRoomId(roomId)) {
                if ( reserveItem.getPeriod().getStartTime().after(r.getPeriod().getStartTime()) &&
                        reserveItem.getPeriod().getStartTime().before(r.getPeriod().getEndTime()) &&
                        reserveItem.getPeriod().getEndTime().after(r.getPeriod().getStartTime()) &&
                        reserveItem.getPeriod().getEndTime().before(r.getPeriod().getEndTime()) ) {
                    throw new RoomBookedException(String.valueOf(roomId));
                }
                if ( reserveItem.getPeriod().getStartTime().equals(r.getPeriod().getStartTime()) ||
                        reserveItem.getPeriod().getEndTime().equals(r.getPeriod().getEndTime()) )  {
                    throw new RoomBookedException(String.valueOf(roomId));
                }
                if ( reserveItem.getPeriod().getStartTime().equals(r.getPeriod().getStartTime()) &&
                        reserveItem.getPeriod().getEndTime().before(r.getPeriod().getEndTime()) ) {
                    throw new RoomBookedException(String.valueOf(roomId));
                }
                if (reserveItem.getPeriod().getStartTime().after( r.getPeriod().getStartTime()) &&
                            reserveItem.getPeriod().getStartTime().before(r.getPeriod().getEndTime()) &&
                            reserveItem.getPeriod().getEndTime().equals(r.getPeriod().getEndTime()) ) {
                        throw new RoomBookedException(String.valueOf(roomId));
                }
                if (reserveItem.getPeriod().getStartTime().before(r.getPeriod().getStartTime()) &&
                    reserveItem.getPeriod().getStartTime().before(r.getPeriod().getEndTime()) &&
                    reserveItem.getPeriod().getEndTime().after(r.getPeriod().getEndTime())) {
                throw new RoomBookedException(String.valueOf(roomId));
                }
                if (reserveItem.getPeriod().getStartTime().after(r.getPeriod().getStartTime()) &&
                        reserveItem.getPeriod().getStartTime().before(r.getPeriod().getEndTime()) &&
                        reserveItem.getPeriod().getEndTime().after(r.getPeriod().getEndTime())) {
                        throw new RoomBookedException(String.valueOf(roomId));
                }
                if (reserveItem.getPeriod().getStartTime().before(r.getPeriod().getStartTime()) &&
                    reserveItem.getPeriod().getEndTime().before(r.getPeriod().getEndTime()) &&
                    reserveItem.getPeriod().getEndTime().after(r.getPeriod().getStartTime())) {
                        throw new RoomBookedException(String.valueOf(roomId));
                }

        }
        return ResponseEntity.ok(clientService.createReservation(reserveItem, roomId));
    }

    @DeleteMapping("/reservation/delete")
    public ResponseEntity<Object> deleteReservation(@RequestParam long reservationId, @RequestParam long roomId,
                                                    @RequestParam long userId, Authentication authentication) {
        Client person = clientRepository.findClientById(userId);
        if (!authentication.getPrincipal().equals(person.getEmail()) ||
                !authentication.getCredentials().equals(person.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        clientService.deleteReservation(reservationId, person.getId(), roomId);
        return new ResponseEntity<>(new ErrorTemplate(String.format("Reservation %d deleted", reservationId) ),
                HttpStatus.OK);
    }
}
