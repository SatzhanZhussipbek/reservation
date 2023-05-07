package jusan.reservation.security;

import jusan.reservation.entity.Client;
import jusan.reservation.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsService implements UserDetailsService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client person = clientRepository.findClientByName(email);
        if (person == null) {
            throw new UsernameNotFoundException("email " + email + " is not found");
        }
        return new ClientDetails(person);
    }
}

