package com.forexservice.forexservice.respositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forexservice.forexservice.pojos.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long>{
	Optional<Currency> findByCode(String code);
}
