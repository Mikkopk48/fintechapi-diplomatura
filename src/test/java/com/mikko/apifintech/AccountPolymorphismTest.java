package com.mikko.apifintech;

import com.mikko.apifintech.exception.InsufficientFundsException;
import com.mikko.apifintech.model.Account;
import com.mikko.apifintech.model.AccountType;
import com.mikko.apifintech.model.CheckingAccount;
import com.mikko.apifintech.model.SavingsAccount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountPolymorphismTest {

    @Test
    void sameWithdrawCall_behavesDifferentlyDependingOnAccountType() {
        Account savings = new SavingsAccount();
        Account checking = new CheckingAccount();
        savings.deposit(new BigDecimal("100"));
        checking.deposit(new BigDecimal("100"));

        BigDecimal amount = new BigDecimal("300");

        assertThrows(InsufficientFundsException.class, () -> savings.withdraw(amount));
        checking.withdraw(amount);
        assertEquals(0, new BigDecimal("-200").compareTo(checking.getBalance()));
    }

    @Test
    void deposit_rejectsZeroAndNegativeAmounts() {
        Account account = new SavingsAccount();

        assertThrows(IllegalArgumentException.class, () -> account.deposit(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-1")));
    }

    @Test
    void withdraw_rejectsZeroAndNegativeAmounts() {
        Account account = new CheckingAccount();

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("-1")));
    }

    @Test
    void getType_identifiesEachSubclass() {
        assertEquals(AccountType.SAVINGS, new SavingsAccount().getType());
        assertEquals(AccountType.CHECKING, new CheckingAccount().getType());
    }
}