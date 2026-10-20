package com.mikko.apifintech.dto;

import com.mikko.apifintech.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String accountNumber,
        AccountType type,
        BigDecimal balance,
        Long userId,
        LocalDateTime createdAt
) {}