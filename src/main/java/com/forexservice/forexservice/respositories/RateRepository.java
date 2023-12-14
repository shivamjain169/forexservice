package com.forexservice.forexservice.respositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forexservice.forexservice.pojos.Rate;

public interface RateRepository extends JpaRepository<Rate, Long>{
	
}
