package com.mikko.apifintech.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAccountRequest(
        @NotBlank String accountNumber
) {}