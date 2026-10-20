package com.mikko.apifintech.mapper;

import com.mikko.apifintech.dto.AccountResponse;
import com.mikko.apifintech.dto.CreateAccountRequest;
import com.mikko.apifintech.dto.CreateUserRequest;
import com.mikko.apifintech.dto.UserResponse;
import com.mikko.apifintech.model.Account;
import com.mikko.apifintech.model.CheckingAccount;
import com.mikko.apifintech.model.SavingsAccount;
import com.mikko.apifintech.model.User;

import java.util.UUID;

public final class Mapper {

    private Mapper() {}

    public static User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    public static Account toEntity(CreateAccountRequest request) {
        Account account = switch (request.type()) {
            case SAVINGS -> new SavingsAccount();
            case CHECKING -> new CheckingAccount();
        };
        account.setAccountNumber(request.type().name().substring(0, 3)
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return account;
    }

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getType(),
                account.getBalance(),
                account.getOwner().getId(),
                account.getCreatedAt()
        );
    }
}