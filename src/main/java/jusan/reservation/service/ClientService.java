package jusan.reservation.service;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.ReserveItem;
import jusan.reservation.entity.Room;
import jusan.reservation.exception.ErrorTemplate;
import jusan.reservation.exception.ReservationsNotFoundException;
import jusan.reservation.model.JwtResponse;
import jusan.reservation.model.Role;
import jusan.reservation.repository.ClientRepository;
import jusan.reservation.repository.ReserveItemRepository;
import jusan.reservation.repository.RoomRepository;
import jusan.reservation.security.ClientDetails;
import jusan.reservation.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    private ReserveItemRepository reserveItemRepository;

    private RoomRepository roomRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager,
                         ReserveItemRepository reserveItemRepository, RoomRepository roomRepository) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.reserveItemRepository = reserveItemRepository;
        this.roomRepository = roomRepository;
    }

    public List<ReserveItem> getReservations(long userId) {
        if (clientRepository.findClientById(userId) != null) {
            List<ReserveItem> list = reserveItemRepository.getReserveItemsByUserId(userId);
            return list;
        }
        throw new ReservationsNotFoundException(userId);
    }

    public ReserveItem createReservation(ReserveItem item) {
        ReserveItem newReservation = new ReserveItem(item.getReservationId(), item.getPeriod(), item.getUserId(),
                item.getDescription(), item.getRoom());
        reserveItemRepository.save(newReservation);
        return newReservation;
    }

    public void deleteReservation(long reservationId, long userId) {
        ReserveItem item = reserveItemRepository.getReserveItemByReservationIdAndUserId(reservationId, userId);
        reserveItemRepository.delete(item);
    }

    public Room createRoom(Room room) {
        Room newRoom = new Room(room.getId(), room.getDescription(), room.getPhotos(), room.getType(),
                room.getCapacity(), room.getFloor(), room.getReservationList());
        roomRepository.save(newRoom);
        return newRoom;
    }

    public String deleteRoom(long userId, Room room) {
        roomRepository.delete(room);
        return String.format("The room "+room.getId()+" has been deleted");
    }

    public ResponseEntity<Object> register(String name, String surname, String email, String password) {
        if (clientRepository.findClientByName(email) != null) {
            System.out.println("There is already a user with such a name.");
        }
        Client user = new Client();
        user.setName(name); user.setSurname(surname);
        user.setEmail(email); user.setPassword(passwordEncoder.encode(password));
        if (clientRepository.findClientById(1) == null) {
            user.setRole(Role.ADMIN);
        }
        else {
            user.setRole(Role.USER);
        }
        clientRepository.save(user);
        return new ResponseEntity<>(new ErrorTemplate(String.format("User %d created", user.getId())),
                HttpStatus.OK);
    }

    public JwtResponse authenticateUser(String email1, String password1) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email1, password1));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception(e.getMessage()+" "+e.getLocalizedMessage());
        }
        Client user = clientRepository.findClientByName(email1);
        if (user == null) {
            throw new UsernameNotFoundException(email1);
        }
        ClientDetails clientDetails = new ClientDetails(user);
        String jwtToken = jwtService.generateToken(clientDetails);
        return new JwtResponse(jwtToken);
    }
}
