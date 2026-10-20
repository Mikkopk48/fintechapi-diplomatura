package com.mikko.apifintech.model;

import com.mikko.apifintech.exception.InsufficientFundsException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account{

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw  new IllegalArgumentException("El monto a retirar debe ser positivo");
        }
        if (amount.compareTo(getBalance())> 0){
            throw  new InsufficientFundsException("Saldo insufuciente");
        }
    }
    @Override
    public AccountType getType() {
        return AccountType.SAVINGS;
    }
}
