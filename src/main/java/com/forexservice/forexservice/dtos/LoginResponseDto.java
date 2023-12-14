package com.forexservice.forexservice.dtos;

import com.forexservice.forexservice.pojos.User;

import lombok.Data;
@Data
public class LoginResponseDto {
	private String token;
    private User user;

    public LoginResponseDto(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
