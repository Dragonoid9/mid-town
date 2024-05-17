package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.requestDto.ProfileRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;
import com.rac.ktm.midtown.dto.responseDto.ProfileResponseDto;

public interface UserService {

    public UserDto registerUser(UserDto userDto);
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto);
    public ProfileResponseDto profileRequest(ProfileRequestDto profileRequestDto);

    boolean verifyPassword(String userName, String currentPassword);


    void updateProfile(ProfileResponseDto profileResponseDto);
}
