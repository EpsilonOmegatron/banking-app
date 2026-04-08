package net.javapractice.banking_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javapractice.banking_app.dto.AccountDto;
import net.javapractice.banking_app.dto.TransferDataDto;
import net.javapractice.banking_app.service.AccountService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PatchMapping("{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        return ResponseEntity.ok(accountService.deposit(id, request.get("amount")));
    }

    @PatchMapping("{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        return ResponseEntity.ok(accountService.withdraw(id, request.get("amount")));
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully!");
    }

    @PostMapping("transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransferDataDto transferDataDto) {
        accountService.transferFunds(transferDataDto);
        return ResponseEntity.ok("Transfer successful!");
    }
}
