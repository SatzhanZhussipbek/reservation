package jusan.reservation.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrame {


    private String id = UUID.randomUUID().toString();

    private Date startTime;

    private Date endTime;

}
