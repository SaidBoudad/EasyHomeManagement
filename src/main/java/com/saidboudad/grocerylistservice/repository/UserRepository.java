package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by username and by email.
    User findByUsername(String username);
    User findByEmail(String email);
    Page<User> findByUsernameContains(String kw, Pageable pageable);

    //Second way using  JPQL query
//    @Query("select user from User user  where user.username like :x")
//    Page<User> search(@Param("x") String keyword, Pageable pageable);

}