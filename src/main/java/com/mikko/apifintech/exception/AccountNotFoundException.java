package com.mikko.apifintech.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Cuenta no encontrada con id: " + id);
    }
}