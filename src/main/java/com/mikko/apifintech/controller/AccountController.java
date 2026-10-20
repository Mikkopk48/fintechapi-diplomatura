package com.mikko.apifintech.controller;


import com.mikko.apifintech.dto.AccountResponse;
import com.mikko.apifintech.dto.AmountRequest;
import com.mikko.apifintech.dto.CreateAccountRequest;
import com.mikko.apifintech.dto.UpdateAccountRequest;
import com.mikko.apifintech.mapper.Mapper;
import com.mikko.apifintech.model.Account;
import com.mikko.apifintech.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class AccountController {
private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping
    public List<AccountResponse>findAll(){
        return accountService.findAll().stream()
                .map(Mapper::toResponse)
                .toList();
    }
    @GetMapping("/{id}")
    public AccountResponse findById(@PathVariable Long id){
        return Mapper.toResponse(accountService.findById(id));
    }
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request){
        Account created = accountService.create(Mapper.toEntity(request), request.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Mapper.toResponse(created));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/depositos")
    public AccountResponse deposit(@PathVariable Long id, @Valid @RequestBody AmountRequest request) {
        return Mapper.toResponse(accountService.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/retiros")
    public AccountResponse withdraw(@PathVariable Long id, @Valid @RequestBody AmountRequest request) {
        return Mapper.toResponse(accountService.withdraw(id, request.amount()));
    }
    @PutMapping("/{id}")
    public AccountResponse update(@PathVariable Long id, @Valid @RequestBody UpdateAccountRequest request) {
        return Mapper.toResponse(accountService.update(id, request.accountNumber()));
    }
}
