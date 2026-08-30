package com.igor.EcoPathAPI.repository;

import com.igor.EcoPathAPI.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String username);
}
