package jusan.reservation.service;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.ReserveItem;
import jusan.reservation.entity.Room;
import jusan.reservation.repository.ClientRepository;
import jusan.reservation.repository.ReserveItemRepository;
import jusan.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.mail.*;

import java.util.List;

@Component
public class MailService {

    private ClientRepository clientRepository;

    private ReserveItemRepository reserveItemRepository;

    private JavaMailSender emailSender;

    private RoomRepository roomRepository;

    @Autowired
    public MailService(JavaMailSender emailSender, ClientRepository clientRepository,
                       ReserveItemRepository reserveItemRepository, RoomRepository roomRepository) {
        this.emailSender = emailSender;
        this.clientRepository = clientRepository;
        this.reserveItemRepository = reserveItemRepository;
        this.roomRepository = roomRepository;
    }

    public void sendMessage(long userId, long roomId) {
        Client user = clientRepository.findClientById(userId);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("satzhanbek@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("The reservation has been created.");
        message.setText("Your room: " + roomId);
        emailSender.send(message);
        System.out.println("Received a message");
    }

}
