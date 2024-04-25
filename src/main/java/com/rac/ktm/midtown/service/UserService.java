package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.dto.UserDto;
import com.rac.ktm.midtown.dto.requestDto.LoginRequestDto;
import com.rac.ktm.midtown.dto.responseDto.LoginResponseDto;

public interface UserService {

    public UserDto registerUser(UserDto userDto);
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto);
}
