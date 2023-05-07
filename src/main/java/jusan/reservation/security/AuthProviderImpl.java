package jusan.reservation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    private ClientDetailsService clientDetailsService;


    @Autowired
    public AuthProviderImpl(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        UserDetails personalDetails = clientDetailsService.loadUserByUsername(userName);
        String password = authentication.getCredentials().toString();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        if (!encoder.matches(password, personalDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password!");
        }
        return new UsernamePasswordAuthenticationToken(userName, password,
                personalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
