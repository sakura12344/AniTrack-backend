package com.anitrack.anitrack.repository;

import com.anitrack.anitrack.entity.User;
import com.anitrack.anitrack.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByEmailContaining(String email);

    List<User> findByUsernameContaining(String username);

    List<User> searchByUsernameAndEmail(String username, String email);
}
