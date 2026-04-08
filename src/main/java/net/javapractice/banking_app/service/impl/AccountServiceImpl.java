package net.javapractice.banking_app.service.impl;

import org.springframework.stereotype.Service;

import net.javapractice.banking_app.dto.AccountDto;
import net.javapractice.banking_app.entity.Account;
import net.javapractice.banking_app.mapper.AccountMapper;
import net.javapractice.banking_app.repository.AccountRepository;
import net.javapractice.banking_app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

}
