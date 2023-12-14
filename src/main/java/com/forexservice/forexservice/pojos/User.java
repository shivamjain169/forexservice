package com.forexservice.forexservice.pojos;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "users")
@Data	
public class User {
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	  	@Column( nullable = false)
	    private String firstname;
	  	
	  	@Column( nullable = false)
	    private String lastname;
	  	
	  	@Column(unique = true, nullable = false)
	    private String username;

	    @Column(nullable = false)
	    private String password; // Password should be hashed and stored securely
	    
	  	@Column(nullable = false)
	    private String country;

	    // Other user details like name, email, etc.

	    @OneToMany(mappedBy = "sender")
	    private List<Transaction> sentTransactions;

	    @OneToMany(mappedBy = "recipient")
	    private List<Transaction> receivedTransactions;

}
