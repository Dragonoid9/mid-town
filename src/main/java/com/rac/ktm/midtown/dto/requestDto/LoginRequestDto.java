package com.rac.ktm.midtown.dto.requestDto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String identifier;
    private String password;
}
