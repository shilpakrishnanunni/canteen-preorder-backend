package org.example.CanteenPreorderBackend.repository;

import org.example.CanteenPreorderBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    userRepository.findAll();
//    userRepository.findById(id);
//    userRepository.save(user);
//    userRepository.delete(user);
//    userRepository.existsById(id);
}
