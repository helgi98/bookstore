package org.example.bookstore.repo;

import org.example.bookstore.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {

    boolean existsByUserName(String userName);

    Optional<UserEntity> findByUserName(String userName);

}
