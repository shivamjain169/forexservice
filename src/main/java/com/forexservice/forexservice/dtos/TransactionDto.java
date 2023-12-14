package com.forexservice.forexservice.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionDto {
	 private Long id;
	    private UserDto sender;
	    private UserDto recipient;
	    private CurrencyDto currency;
	    private BigDecimal amount;
	    private LocalDateTime timestamp;
}
