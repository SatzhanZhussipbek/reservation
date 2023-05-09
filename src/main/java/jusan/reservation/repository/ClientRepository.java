package jusan.reservation.repository;

import jusan.reservation.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import jusan.reservation.entity.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query(value = "select c from Client c where c.id = ?1")
    Client findClientById(long userId);

    @Query(value = "select c from Client c where c.email = ?1")
    Client findClientByEmail(String email);

    @Query(value = "select c from Client c where c.role = ?1")
    Client findClientByRole(Role role);
}
