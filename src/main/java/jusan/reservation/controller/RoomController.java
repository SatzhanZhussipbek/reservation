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

    @PostMapping("/reservation/add")
    public ResponseEntity<Object> addReservation(@RequestParam long roomId, @RequestBody ReserveItem reserveItem) {
        if (reserveItemRepository.findById(reserveItem.getReservationId()).isPresent()) {
            for (ReserveItem r: reserveItemRepository.findAll()) {
                if (r.getRoomId()==reserveItem.getRoomId()) {
                    if ( r.getPeriod().getStartTime().compareTo(reserveItem.getPeriod().getStartTime()) == 0) {
                        if (r.getPeriod().getEndTime().compareTo(reserveItem.getPeriod().getEndTime()) == 0) {
                            throw new RoomBookedException("The room is already booked during this time period.");
                        }
                        throw new RoomBookedException("The room is already booked during this time period.");
                    }
                    if (r.getPeriod().getStartTime().compareTo(reserveItem.getPeriod().getEndTime()) < 0 &&
                    r.getPeriod().getEndTime().compareTo(reserveItem.getPeriod().getEndTime()) > 0) {
                        throw new RoomBookedException("The room is already booked during this time period.");
                    }
                    if (r.getPeriod().getEndTime().compareTo(reserveItem.getPeriod().getStartTime()) > 0 &&
                    r.getPeriod().getEndTime().compareTo(reserveItem.getPeriod().getEndTime()) < 0) {
                        throw new RoomBookedException("The room is already booked during this time period.");
                    }

                }

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
