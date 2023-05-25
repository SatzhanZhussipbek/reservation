package jusan.reservation.service;

import jusan.reservation.entity.PhotoItem;
import jusan.reservation.entity.Room;
import jusan.reservation.repository.PhotoRepository;
import jusan.reservation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Service
@Slf4j
public class ImageService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PhotoRepository photoRepository;

    public ResponseEntity<String> getUploadUrl(String photoName, long roomId) {
        photoName = photoName.replace(".", generateRandom(4));
        String resourceUrl = "https://cloud-api.yandex.net/v1/disk/resources/upload?path=/jusan-images/"+photoName;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "OAuth y0_AgAAAABqlYjJAADLWwAAAADiyzHyE5RslkIiSXqVnnHfbJ1jru7DphI");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
        PhotoItem newPhoto = new PhotoItem(roomId, photoName);
        //log.error("---------------->" + resourceUrl);
        photoRepository.save(newPhoto);
        Room newRoom = roomRepository.getRoomById(roomId);
        newRoom.getPhotos().add(newPhoto);
        //roomRepository.save(newRoom);
        return response;
    }

    public ResponseEntity<?> uploadPhoto(String href, String method, byte[] image) throws IOException {
        String resourceUrl = href;
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(image, headers);
        ResponseEntity<byte[]> response = null;
        if (method.equals("PUT")) {
            response = restTemplate2.exchange(resourceUrl, HttpMethod.PUT, requestEntity, byte[].class);
        }
        if (method.equals("POST")) {
            response = restTemplate2.exchange(resourceUrl, HttpMethod.POST, requestEntity, byte[].class);
        }
        if (method.equals("PATCH")) {
            response = restTemplate2.exchange(resourceUrl, HttpMethod.PATCH, requestEntity, byte[].class);
        }
        return response;
    }

    public ResponseEntity<?> getPhoto(String path) {
        PhotoItem photo = photoRepository.findPhotoItemByName(path.substring(0, path.length()-5));
        StringBuilder resourceUrl2 = new StringBuilder("https://cloud-api.yandex.net/v1/disk/resources/download?path=/jusan-images/");
        resourceUrl2.append(photo.getName());
        RestTemplate restTemplate3 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.set("Authorization", "OAuth y0_AgAAAABqlYjJAADLWwAAAADiyzHyE5RslkIiSXqVnnHfbJ1jru7DphI");
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity(headers2);
        ResponseEntity<String> response = restTemplate3.exchange(resourceUrl2.toString(), HttpMethod.GET, request, String.class);
        return response;
    }


    public static String generateRandom(int num) {
        int length = num;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return generatedString+".";
    }

}
