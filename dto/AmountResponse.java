package com.mikko.apifintech.dto;

import com.mikko.apifintech.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AmountResponse(
        Long id,
        String accountNumber,
        Account type,
        BigDecimal balance,
        Long userId,
        LocalDateTime createdAt
) {
}
