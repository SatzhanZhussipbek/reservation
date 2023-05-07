package jusan.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Rooms")
public class Room {
    @Id
    @Column(name="id")
    private long id;

    @Column(name="description")
    private String description;

    @Column(name = "photos")
    @Lob
    private byte[] photos = new byte[10];

    @Column(name = "type")
    private String type;

    @Column(name = "capacity")
    private long capacity;

    @Column(name = "floor")
    private long floor;

    @Column(name = "photos")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    private List<ReserveItem> reservationList;


}
