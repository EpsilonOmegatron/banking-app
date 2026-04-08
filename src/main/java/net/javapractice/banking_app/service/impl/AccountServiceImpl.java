package net.javapractice.banking_app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.javapractice.banking_app.dto.AccountDto;
import net.javapractice.banking_app.dto.TransactionDto;
import net.javapractice.banking_app.dto.TransferDataDto;
import net.javapractice.banking_app.entity.Account;
import net.javapractice.banking_app.entity.Transaction;
import net.javapractice.banking_app.mapper.AccountMapper;
import net.javapractice.banking_app.mapper.TransactionMapper;
import net.javapractice.banking_app.repository.AccountRepository;
import net.javapractice.banking_app.repository.TransactionRepository;
import net.javapractice.banking_app.service.AccountService;
import net.javapractice.banking_app.exception.AccountException;
import net.javapractice.banking_app.exception.MathException;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    private final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(affectedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account does not exist."));

        if (account.getBalance() < amount) {
            throw new MathException("Insufficient Balance.");
        }

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

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

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferDataDto.senderId());
        transaction.setAmount(transferDataDto.amount());
        transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDto> getAllTransactionsForAccount(Long id) {
        accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist."));
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(id);
        return transactions.stream().map((transaction) -> TransactionMapper.mapToTransactionDto(transaction))
                .collect(Collectors.toList());
    }

}
