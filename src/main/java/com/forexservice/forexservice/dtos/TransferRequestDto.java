package com.forexservice.forexservice.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferRequestDto {
	 	private Long senderId;
	    private Long recipientId;
	    private BigDecimal amount;
}
