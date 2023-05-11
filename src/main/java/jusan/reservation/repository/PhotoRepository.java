package jusan.reservation.repository;

import jusan.reservation.entity.Client;
import jusan.reservation.entity.PhotoItem;
import jusan.reservation.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends CrudRepository<PhotoItem, Long> {

    @Query(value = "select p from PhotoItem p where p.photoId = ?1")
    PhotoItem findPhotoItemByPhotoId(long photoId);

    @Query(value = "select p from PhotoItem p where p.roomId = ?1")
    PhotoItem findPhotoItemByRoomId(long roomId);

    @Query(value = "select p from PhotoItem p where p.name like %:name%")
    PhotoItem findPhotoItemByName(@Param("name") String name);
}
