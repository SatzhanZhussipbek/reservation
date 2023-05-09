package jusan.reservation.model;

import jakarta.persistence.*;
import jusan.reservation.entity.ReserveItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {

        private long id;

        private String description;

        private String photo;

        private String type;

        private long capacity;

        private long floor;

        private List<ReserveItem> reservationList;
    }
