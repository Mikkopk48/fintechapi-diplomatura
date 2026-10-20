package com.mikko.apifintech.controller;

import com.mikko.apifintech.dto.CreateUserRequest;
import com.mikko.apifintech.dto.ErrorResponse;
import com.mikko.apifintech.dto.UserResponse;
import com.mikko.apifintech.mapper.Mapper;
import com.mikko.apifintech.model.User;
import com.mikko.apifintech.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Alta y consulta de usuarios titulares de cuentas")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un usuario",
            description = "Registra un usuario nuevo. El email debe ser único.")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        User created = userService.create(Mapper.toEntity(request));
        return Mapper.toResponse(created);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Listado de usuarios")
    public List<UserResponse> findAll() {
        return userService.findAll().stream().map(Mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por id")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public UserResponse findById(@PathVariable Long id) {
        return Mapper.toResponse(userService.findById(id));
    }
}