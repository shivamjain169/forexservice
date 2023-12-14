package com.forexservice.forexservice.dtos;

import javax.persistence.Column;

import lombok.Data;
@Data
public class UserDto {
	private Long id;
	private String firstname;
  	private String lastname;
  	private String username;
    private String password; // Password should be hashed and stored securely
    private String country;
    
}
