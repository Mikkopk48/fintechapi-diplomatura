package com.mikko.apifintech.exception;

public class DuplicateAccountNumberException extends RuntimeException {
    public DuplicateAccountNumberException(String accountNumber) {
        super("Ya existe una cuenta con el número: " + accountNumber);
    }
}