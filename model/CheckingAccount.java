package com.mikko.apifintech.model;

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

    @Column(name = "overdraft_limit", nullable = false)
    private BigDecimal overdraftLimit = BigDecimal.valueOf(500);

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");
        }
        BigDecimal saldoDisponible = getBalance().add(overdraftLimit);
        if (amount.compareTo(saldoDisponible) > 0) {
            throw new IllegalStateException("Saldo insuficiente, incluso considerando el descubierto");
        }
        setBalance(getBalance().subtract(amount));
    }


}