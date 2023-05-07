package jusan.reservation.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import jusan.reservation.entity.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query(value = "select c from Client c where c.id = ?1")
    Client findClientById(long userId);

    @Query(value = "select c from Client c where c.name = ?1")
    Client findClientByName(String email);


}
