package net.javapractice.banking_app.service;

import net.javapractice.banking_app.dto.AccountDto;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);
}
