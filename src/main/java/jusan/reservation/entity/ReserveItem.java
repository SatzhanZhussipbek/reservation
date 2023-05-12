package jusan.reservation.entity;

import jakarta.persistence.*;
import jusan.reservation.model.TimeFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Reserve_item")
public class ReserveItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long reservationId;

    @Column(name = "period")
    private TimeFrame period;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "description")
    private String description;

}
