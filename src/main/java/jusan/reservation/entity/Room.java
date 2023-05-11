package jusan.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhotoItem> photos;

    @Column(name = "type")
    private String type;

    @Column(name = "capacity")
    private long capacity;

    @Column(name = "floor")
    private long floor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReserveItem> reservationList;


}
