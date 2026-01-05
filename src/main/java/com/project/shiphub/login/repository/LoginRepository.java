package com.project.shiphub.login.repository;

import com.project.shiphub.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailAndPassword(String email, Long password);

    boolean existsByEmailAndPassword(String email, Long password);
}
