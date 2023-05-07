package jusan.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtRequest {

    private String name;

    private String surname;

    private String email;

    private String password;
}
