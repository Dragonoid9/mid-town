package com.rac.ktm.midtown.service;

import com.rac.ktm.midtown.dto.UserDto;

public interface UserService {

    public UserDto registerUser(UserDto userDto);
    public boolean authenticateUser(String identifier, String password);
}
