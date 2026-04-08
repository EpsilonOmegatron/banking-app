package net.javapractice.banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javapractice.banking_app.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}