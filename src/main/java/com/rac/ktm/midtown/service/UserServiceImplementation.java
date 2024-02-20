package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.entity.User;
import com.rac.ktm.midtown.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        return userDto;
    }

    @Override
    public boolean authenticateUser(String identifier, String password) {
        if (identifier == null || identifier.isEmpty()) {
            return false; // Invalid identifier
        }

        // Example authentication logic
        User user = userRepository.findByUserNameOrEmail(identifier,password);

        return user != null && user.getPassword().equals(password); // Authentication successful
        // Authentication failed
    }
}