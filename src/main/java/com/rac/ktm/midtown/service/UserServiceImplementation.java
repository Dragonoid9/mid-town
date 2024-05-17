package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.requestDto.ProfileRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;
import com.rac.ktm.midtown.dto.responseDto.ProfileResponseDto;
import com.rac.ktm.midtown.entity.User;
import com.rac.ktm.midtown.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        String identifier = loginRequestDto.getIdentifier();
        String password = loginRequestDto.getPassword();
        if (identifier == null || identifier.isEmpty()) {
            return null;
        }

        User user = userRepository.findByUserNameOrEmail(identifier, password);

        if (user != null && user.getPassword().equals(password)) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUserName(user.getUserName());
            return loginResponseDto;
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
        String identifier = userName;
        String password = currentPassword;
        User user = userRepository.findByUserNameOrEmail(identifier, password);
        return user != null;
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