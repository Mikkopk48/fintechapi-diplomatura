package com.mikko.apifintech.dto;

import com.mikko.apifintech.model.AccountType;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
        @NotNull Long userId,
        @NotNull AccountType type
) {}
