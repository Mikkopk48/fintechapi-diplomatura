package com.mikko.apifintech.model;

import java.math.BigDecimal;

public interface Operable {
    void deposit(BigDecimal amount);
    void withdraw(BigDecimal amount);
}
