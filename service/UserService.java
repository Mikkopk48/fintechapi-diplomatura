package com.mikko.apifintech.service;

import com.mikko.apifintech.exception.DuplicateEmailException;
import com.mikko.apifintech.exception.UserNotFoundException;
import com.mikko.apifintech.model.User;
import com.mikko.apifintech.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }
        return userRepository.save(user);
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
