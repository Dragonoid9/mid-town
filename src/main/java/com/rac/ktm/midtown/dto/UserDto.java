package com.rac.ktm.midtown.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private short id;
    @NotBlank(message = "Username cannot be Blank.")
    @NotNull(message = "Username cannot be Null.")
    private String userName;
    @NotBlank(message = "Name cannot be Blank.")
    @NotNull(message ="Name cannot be Null.")
    private String name;
    @Email(message = "Email Should be Valid.")
    private String email;
    @Pattern(regexp = "(\\+977|0)(97|98)[0-9]{8}",message ="Enter Valid Phone Number." )
    private String phoneNumber;
    @NotBlank(message = "Password cannot be Blank.")
    @NotNull(message = "Password Cannot be Null.")
    private String password;
}
