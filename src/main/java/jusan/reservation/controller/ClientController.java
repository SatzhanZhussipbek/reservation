package jusan.reservation.controller;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.Room;
import jusan.reservation.model.JwtRequest;
import jusan.reservation.model.Role;
import jusan.reservation.model.RoomDTO;
import jusan.reservation.model.SignInRequest;
import jusan.reservation.repository.ClientRepository;
import jusan.reservation.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
@Slf4j
public class ClientController {
    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateData (@RequestBody SignInRequest signInRequest) throws Exception {
        return ResponseEntity.ok(clientService.authenticateUser(signInRequest.getEmail(),
                signInRequest.getPassword()));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody JwtRequest client) {
        return ResponseEntity.ok(clientService.register(client.getName(), client.getSurname(), client.getEmail(), client.getPassword()));
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getReservations(@RequestParam long userId) {
        return ResponseEntity.ok(clientService.getReservations(userId));
    }

    @PostMapping("/rooms/add")
    public ResponseEntity<?> createRoom(@RequestBody RoomDTO roomDTO, Authentication authentication) throws IOException {
        Client person = clientRepository.findClientById(roomDTO.getReservationList().get(0).getUserId());
        if ( !authentication.getPrincipal().equals(person.getEmail()) ||
                !authentication.getCredentials().equals(person.getPassword()) )  {
            throw new AccessDeniedException("Access denied. You entered the wrong id.");
        }
        if (person.getRole().equals(Role.USER)) {
            throw new AccessDeniedException("Access denied. Only admins can create rooms.");
        }
        return ResponseEntity.ok(clientService.createRoom(roomDTO));
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
            throw new AccessDeniedException("Access denied. Only admins can erase rooms.");
        }
        return ResponseEntity.ok(clientService.deleteRoom(userId, room));
    }


}
