package jusan.reservation.service;

import jusan.reservation.entity.Room;
import jusan.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<Room> getFilteredRoomsById() {
        List<Room> list = roomRepository.findAll(Sort.by("id").descending());
        return list;
    }

    public List<Room> getFilteredRoomsByFloor() {
        List<Room> list = roomRepository.findAll(Sort.by("floor"));
        return list;
    }

    public List<Room> getFilteredRoomsByCapacity() {
        List<Room> list = roomRepository.findAll(Sort.by("capacity").descending());
        return list;
    }

    public Room getRoom(long roomId) {
        Room room = roomRepository.getRoomById(roomId);
        return room;
    }
}
