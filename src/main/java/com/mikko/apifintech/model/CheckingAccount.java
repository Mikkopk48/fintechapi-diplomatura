package com.mikko.apifintech.model;

import com.mikko.apifintech.exception.InsufficientFundsException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@DiscriminatorValue("CHECKING")

public class CheckingAccount extends Account {

    @Column(name = "overdraft_limit")
    private BigDecimal overdraftLimit = BigDecimal.valueOf(500);

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");
        }
        BigDecimal availableFunds = getBalance().add(overdraftLimit);
        if (amount.compareTo(availableFunds) > 0) {
            throw new InsufficientFundsException("Saldo insuficiente, incluso considerando el descubierto");
        }
        setBalance(getBalance().subtract(amount));
    }

    @Override
    public AccountType getType() {
        return AccountType.CHECKING;
    }
}