package com.mikko.apifintech;

import com.mikko.apifintech.exception.AccountNotFoundException;
import com.mikko.apifintech.exception.InsufficientFundsException;
import com.mikko.apifintech.model.Account;
import com.mikko.apifintech.model.CheckingAccount;
import com.mikko.apifintech.model.SavingsAccount;
import com.mikko.apifintech.model.User;
import com.mikko.apifintech.repository.AccountRepository;
import com.mikko.apifintech.service.AccountService;
import com.mikko.apifintech.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountService accountService;

    private SavingsAccount savingsWith(String balance) {
        SavingsAccount account = new SavingsAccount();
        account.setBalance(new BigDecimal(balance));
        return account;
    }
    private CheckingAccount checkingWith(String balance) {
        CheckingAccount account = new CheckingAccount();
        account.setBalance(new BigDecimal(balance));
        return account;
    }
    @Test
    void findById_returnsAccount_whenItExists() {
        SavingsAccount account = savingsWith("100");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.findById(1L);

        assertSame(account, result);
    }
    @Test
    void findById_throwsAccountNotFound_whenItDoesNotExist() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findById(99L));
    }
    @Test
    void create_assignsOwnerLoadedFromUserService() {
        User owner = new User();
        SavingsAccount account = new SavingsAccount();
        when(userService.findById(1L)).thenReturn(owner);
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.create(account, 1L);

        assertSame(owner, result.getOwner());
    }
    @Test
    void deposit_increasesBalance() {
        SavingsAccount account = savingsWith("1000");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.deposit(1L, new BigDecimal("500"));

        assertEquals(0, new BigDecimal("1500").compareTo(result.getBalance()));
    }

    @Test
    void deposit_rejectsNegativeAmount_andDoesNotSave() {
        SavingsAccount account = savingsWith("1000");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit(1L, new BigDecimal("-5")));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void withdraw_savings_rejectsWhenBalanceIsNotEnough() {
        SavingsAccount account = savingsWith("100");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw(1L, new BigDecimal("101")));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void withdraw_checking_usesOverdraft() {
        CheckingAccount account = checkingWith("100");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.withdraw(1L, new BigDecimal("500"));

        assertEquals(0, new BigDecimal("-400").compareTo(result.getBalance()));
    }

    @Test
    void withdraw_checking_rejectsWhenOverdraftIsExceeded() {
        CheckingAccount account = checkingWith("100");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw(1L, new BigDecimal("601")));
    }

    @Test
    void delete_throwsAccountNotFound_whenItDoesNotExist() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.delete(99L));
        verify(accountRepository, never()).delete(any());
    }
}
