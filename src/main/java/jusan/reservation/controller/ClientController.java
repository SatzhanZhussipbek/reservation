package jusan.reservation.controller;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.Room;
import jusan.reservation.model.JwtRequest;
import jusan.reservation.model.Role;
import jusan.reservation.repository.ClientRepository;
import jusan.reservation.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateData (@RequestBody JwtRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(clientService.authenticateUser(jwtRequest.getEmail(),
                jwtRequest.getPassword()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok(clientService.register(jwtRequest.getName(), jwtRequest.getSurname(), jwtRequest.getEmail(), jwtRequest.getPassword()));
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getReservations(@RequestParam long userId) {
        return ResponseEntity.ok(clientService.getReservations(userId));
    }

    @PostMapping("/rooms/add")
    public ResponseEntity<?> createRoom(@RequestParam long userId,
                                        @RequestBody Room room, Authentication authentication) {
        Client person = clientRepository.findClientById(userId);
        if ( !authentication.getPrincipal().equals(person.getEmail()) ||
                !authentication.getCredentials().equals(person.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        if (person.getRole().equals(Role.USER)) {
            throw new AccessDeniedException("Access denied. Only admins can create rooms.");
        }
        return ResponseEntity.ok(clientService.createRoom(room));
    }

    @DeleteMapping("/rooms/delete")
    public ResponseEntity<?> deleteRoom(@RequestParam long userId,
                                        @RequestBody Room room, Authentication authentication) {
        Client person1 = clientRepository.findClientById(userId);
        if ( !authentication.getPrincipal().equals(person1.getEmail()) ||
                !authentication.getCredentials().equals(person1.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        if (person1.getRole().equals(Role.USER)) {
            throw new AccessDeniedException("Access denied. Only admins can create rooms.");
        }
        return ResponseEntity.ok(clientService.deleteRoom(userId, room));
    }
}
