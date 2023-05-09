package jusan.reservation;

import jusan.reservation.model.RoomDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = {"jusan.reservation.*"})
public class ReservationApplication {

    public static void main(String[] args) throws IOException {
        RoomDTO room = new RoomDTO();
        room.setPhoto("/home/satzhan.zhussipbek/IdeaProjects/reservation/src/main/resources/mountain.jpg");
        SpringApplication.run(ReservationApplication.class, args);
        byte[] fileContent = FileUtils.readFileToByteArray(new File(room.getPhoto()));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        System.out.println(encodedString);
    }

}
