package jusan.reservation.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${server.jwt-token.secret}")
    private String SECRET_KEY;
    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.putIfAbsent("sub", "");
        claims.putIfAbsent("scope", "app");
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean validate(String username, String token) {
        return (extractUsername(token).equals(username)) && !isExpired(token);
    }
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private Key getSecretKey() {
        byte[] keyInBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyInBytes);
    }
}
