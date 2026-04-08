package net.javapractice.banking_app.mapper;

import net.javapractice.banking_app.dto.TransactionDto;
import net.javapractice.banking_app.entity.Transaction;

public class TransactionMapper {
    public static Transaction mapToTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction(transactionDto.id(), transactionDto.accountId(),
                transactionDto.amount(), transactionDto.transactionType(), transactionDto.timestamp());
        return transaction;
    }

    public static TransactionDto mapToTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto(transaction.getId(), transaction.getAccountId(),
                transaction.getAmount(), transaction.getTransactionType(), transaction.getTimestamp());
        return transactionDto;
    }
}
