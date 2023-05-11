package jusan.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Photo_items")
public class PhotoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long photoId;

    @Column(name = "room_id")
    private long roomId;

    @Column(name = "name")
    private String name;

    public PhotoItem(long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}
