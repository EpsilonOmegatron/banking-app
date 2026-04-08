package net.javapractice.banking_app.dto;

import java.time.LocalDateTime;

public record TransactionDto(Long id, Long accountId, Double amount, String transactionType, LocalDateTime timestamp) {

}
