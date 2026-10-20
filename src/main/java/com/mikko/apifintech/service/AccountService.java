package com.mikko.apifintech.service;

import com.mikko.apifintech.exception.AccountNotFoundException;
import com.mikko.apifintech.model.Account;
import com.mikko.apifintech.model.User;
import com.mikko.apifintech.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }
    public Account create(Account account, Long userId){
        User owner = userService.findById(userId);
        account.setOwner(owner);
        return  accountRepository.save(account);
    }
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = findById(accountId);
        account.deposit(amount);
        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = findById(accountId);
        account.withdraw(amount);
        return accountRepository.save(account);
    }

    public void delete(Long id) {
        Account account = findById(id);
        accountRepository.delete(account);
    }
    public Account update(Long id, String accountNumber) {
        Account account = findById(id);
        account.setAccountNumber(accountNumber);
        return accountRepository.save(account);
    }
}
