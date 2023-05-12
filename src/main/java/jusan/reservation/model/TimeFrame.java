package jusan.reservation.model;

import jakarta.persistence.*;
import jusan.reservation.entity.ReserveItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrame {

    private long clientId;

    private long roomId;

    private Date startTime;

    private Date endTime;

}
