package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Custom query method to find a client by clientName and email.
    Client findByClientNameAndEmail(String clientName, String email);
    Client findByClientName(String clientName);
    Client findByEmail(String email);
    Page<Client> findByClientNameContains(String kw, Pageable pageable);

    //Second way using JPQL query
//    @Query("select client from Client client  where client.clientName like :x")
//    Page<Client> search(@Param("x") String keyword, Pageable pageable);

}