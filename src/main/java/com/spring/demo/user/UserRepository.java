package com.spring.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u.id from User u order by u.id asc")
    List<Integer> findUsersId();

    @Query("select u from User u where u.id=:id")
    Optional<User> findByUserId(@Param("id") int id);


    @Query("select u from User u where u.name=:name")
    Optional<User> findByUserName(@Param("name") String name);
}
