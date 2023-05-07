package jusan.reservation.service;

import jusan.reservation.entity.Room;
import jusan.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        List<Room> list = roomRepository.getRooms();
        return list;
    }

    public Room getRoom(long roomId) {
        Room room = roomRepository.getRoomById(roomId);
        return room;
    }
}
