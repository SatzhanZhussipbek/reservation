package jusan.reservation.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import jusan.reservation.entity.Room;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    /*@Transactional
    @Modifying
    @Query(value = "update Room r set r.status=?1 where i.id=?2")
    void updateInvoiceStatus(String status, String Id);*/

    @Query(value = "select r from Room r")
    List<Room> getRooms();

    @Query(value = "select r from Room r where r.id=?1")
    Room getRoomById(long roomId);


}
