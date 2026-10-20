package com.mikko.apifintech.controller;


import com.mikko.apifintech.dto.CreateUserRequest;
import com.mikko.apifintech.dto.UserResponse;
import com.mikko.apifintech.mapper.Mapper;
import com.mikko.apifintech.model.User;
import com.mikko.apifintech.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {
private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request){
        User created = userService.create(Mapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(Mapper.toResponse(created));
    }
    @GetMapping
    public List<UserResponse> findAll(){
        return userService.findAll().stream()
                .map(Mapper::toResponse)
                .toList();
    }
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id){
        return Mapper.toResponse(userService.findById(id));
    }
}
