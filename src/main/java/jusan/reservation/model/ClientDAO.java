package jusan.reservation.model;


import jusan.reservation.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ClientDAO {

    private long id;

    private String email;

    public ClientDAO(Client person) {
        this.id = person.getId();
        this.email = person.getEmail();
    }
}
