package net.javapractice.banking_app.service;

import java.util.List;

import net.javapractice.banking_app.dto.AccountDto;
import net.javapractice.banking_app.dto.TransactionDto;
import net.javapractice.banking_app.dto.TransferDataDto;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccount(Long id);

    AccountDto deposit(Long id, Double amount);

    AccountDto withdraw(Long id, Double amount);

    List<AccountDto> getAllAccounts();

    void deleteAccount(Long id);

    void transferFunds(TransferDataDto transferDataDto);

    List<TransactionDto> getAllTransactionsForAccount(Long id);
}
