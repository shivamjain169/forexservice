package com.forexservice.forexservice.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forexservice.forexservice.pojos.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findBySenderIdOrRecipientId(Long senderId, Long recipientId);
}
