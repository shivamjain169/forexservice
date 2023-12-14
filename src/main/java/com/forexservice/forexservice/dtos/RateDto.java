package com.forexservice.forexservice.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RateDto {
	private Long id;
    private CurrencyDto currency;
    private BigDecimal rate;
    private LocalDate date;
}
