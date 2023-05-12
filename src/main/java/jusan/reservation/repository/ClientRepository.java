package jusan.reservation.repository;

import jusan.reservation.model.ClientDTO;
import jusan.reservation.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import jusan.reservation.entity.Client;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "select c from Client c where c.id = ?1")
    Client findClientById(long userId);

    @Query(value = "select c from Client c where c.email = ?1")
    Client findClientByEmail(String email);

    @Query(value = "select c from Client c where c.role = ?1")
    Client findClientByRole(Role role);

    List<ClientDTO> findAllBy();
}
