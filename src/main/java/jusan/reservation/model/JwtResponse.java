package jusan.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtResponse {

    private long userId;

    private Role role;

    private String jwtToken;

}
