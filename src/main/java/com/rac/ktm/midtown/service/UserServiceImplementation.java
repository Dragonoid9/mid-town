package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.requestDto.ProfileRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;
import com.rac.ktm.midtown.dto.responseDto.ProfileResponseDto;
import com.rac.ktm.midtown.entity.User;
import com.rac.ktm.midtown.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder   = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {

        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encryptedPassword);

        User user = new User();
        user.setName(userDto.getName());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        user.setRole("user");
        userRepository.save(user);
        return userDto;
    }

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByIdentifier(loginRequestDto.getIdentifier());
        if (user != null && passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            // Create and return a response DTO or token as per your requirement
            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setUserName(user.getUserName());
            responseDto.setRole(user.getRole());
            return responseDto;
        }
        return null;
    }

    @Override
    public ProfileResponseDto profileRequest(ProfileRequestDto profileRequestDto) {
        String userName = profileRequestDto.getUserName();

        User user = userRepository.findByUserName(userName);

        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setUserName(user.getUserName());
        profileResponseDto.setName(user.getName());
        profileResponseDto.setEmail(user.getEmail());
        profileResponseDto.setPhoneNumber(user.getPhoneNumber());

        return profileResponseDto;
    }

    @Override
    public boolean verifyPassword(String userName, String currentPassword) {
        User user = userRepository.findByUserName(userName);
            // Compare the hashed password in the database with the plaintext password
            return passwordEncoder.matches(currentPassword, user.getPassword());

    }

    @Override
    @Transactional
    public void updateProfile(ProfileResponseDto profileResponseDto) {
        String userName = profileResponseDto.getUserName();
        String name = profileResponseDto.getName();
        String email = profileResponseDto.getEmail();
        String phoneNumber = profileResponseDto.getPhoneNumber();
        userRepository.updateProfile(name, email, phoneNumber, userName);
    }


}