package jusan.reservation.repository;

import jusan.reservation.entity.ReserveItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReserveItemRepository extends CrudRepository<ReserveItem, Long> {
    @Query(value = "select r from ReserveItem r where r.userId = ?1")
    List<ReserveItem> getReserveItemsByUserId(long userId);

    @Query(value = "select r from ReserveItem r where r.reservationId = ?1 and r.userId = ?2")
    ReserveItem getReserveItemByReservationIdAndUserId(long reservationId, long userId);

    ReserveItem getReserveItemByRoomId(long roomId);
}
