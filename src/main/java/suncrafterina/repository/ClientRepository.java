package suncrafterina.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import suncrafterina.domain.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Optional<Client> findById(Long id);

    Optional<Client> findByTitleIgnoreCase(String title);

    @Query(value = "select * from jhi_client c where lower(c.title) like lower(concat('%',?1,'%'))",nativeQuery = true)
    Page<Object[]> findAllClients(String search, Pageable pageable);

    @Query("from Client c where c.id <> ?1 and lower(c.title) = lower(?2) ")
    Optional<Client> findByTitleAlreadyExist(Long id,String title);

}
