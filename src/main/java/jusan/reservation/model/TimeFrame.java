package jusan.reservation.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrame {


    private String id = generateRandom(4);

    private Date startTime;

    private Date endTime;

    public static String generateRandom(int num) {
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(num, useLetters, useNumbers);
        return generatedString+".";
    }

}
