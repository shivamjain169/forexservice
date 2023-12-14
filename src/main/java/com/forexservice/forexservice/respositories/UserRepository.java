package com.forexservice.forexservice.respositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forexservice.forexservice.pojos.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
}
