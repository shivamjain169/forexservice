package com.forexservice.forexservice.respositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forexservice.forexservice.pojos.Currency;
import com.forexservice.forexservice.pojos.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>{
    List<ExchangeRate> findByCurrencyAndDate(Currency currency, LocalDate date);

}
