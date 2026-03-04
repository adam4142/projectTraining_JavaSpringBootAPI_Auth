package org.example.projecttraining.Repository;

import org.example.projecttraining.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User findByToken(String token);
    boolean existsByToken(String token);
}
