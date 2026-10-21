package org.example.CanteenPreorderBackend.service;

import org.example.CanteenPreorderBackend.entities.User;
import org.example.CanteenPreorderBackend.exception.UserNotFoundException;
import org.example.CanteenPreorderBackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
