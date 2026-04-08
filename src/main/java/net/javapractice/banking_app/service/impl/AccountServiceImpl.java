package net.javapractice.banking_app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.javapractice.banking_app.dto.AccountDto;
import net.javapractice.banking_app.dto.TransferDataDto;
import net.javapractice.banking_app.entity.Account;
import net.javapractice.banking_app.mapper.AccountMapper;
import net.javapractice.banking_app.repository.AccountRepository;
import net.javapractice.banking_app.service.AccountService;
import net.javapractice.banking_app.exception.AccountException;
import net.javapractice.banking_app.exception.MathException;

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

    @Override
    public AccountDto getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist."));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, Double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist."));
        account.setBalance(account.getBalance() + amount);
        Account affectedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(affectedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist."));

        if (account.getBalance() < amount) {
            throw new MathException("Insufficient Balance.");
        }

        account.setBalance(account.getBalance() - amount);
        Account affectedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(affectedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist."));
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void transferFunds(TransferDataDto transferDataDto) {
        Account sender = accountRepository.findById(transferDataDto.senderId())
                .orElseThrow(() -> new AccountException("Account does not exist."));
        Account reciever = accountRepository.findById(transferDataDto.recieverId())
                .orElseThrow(() -> new AccountException("Account does not exist."));

        if (sender.getBalance() < transferDataDto.amount()) {
            throw new MathException("Insufficient Balance.");
        }

        reciever.setBalance(reciever.getBalance() + transferDataDto.amount());
        accountRepository.save(reciever);

        sender.setBalance(sender.getBalance() - transferDataDto.amount());
        accountRepository.save(sender);
    }

}
