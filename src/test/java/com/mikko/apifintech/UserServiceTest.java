package com.mikko.apifintech;

import com.mikko.apifintech.exception.DuplicateEmailException;
import com.mikko.apifintech.exception.UserNotFoundException;
import com.mikko.apifintech.model.User;
import com.mikko.apifintech.repository.UserRepository;
import com.mikko.apifintech.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User userWithEmail(String email) {
        User user = new User();
        user.setFirstName("Ana");
        user.setLastName("Perez");
        user.setEmail(email);
        return user;
    }

    @Test
    void create_savesUser_whenEmailIsNew() {
        User user = userWithEmail("ana@mail.com");
        when(userRepository.existsByEmail("ana@mail.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertSame(user, result);
        verify(userRepository).save(user);
    }

    @Test
    void create_throwsDuplicateEmail_whenEmailAlreadyExists_andDoesNotSave() {
        User user = userWithEmail("ana@mail.com");
        when(userRepository.existsByEmail("ana@mail.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_throwsDuplicateEmail_whenDatabaseRejectsUniqueConstraint() {
        User user = userWithEmail("ana@mail.com");
        when(userRepository.existsByEmail("ana@mail.com")).thenReturn(false);
        when(userRepository.save(user)).thenThrow(new DataIntegrityViolationException("unique"));

        assertThrows(DuplicateEmailException.class, () -> userService.create(user));
    }

    @Test
    void findById_returnsUser_whenItExists() {
        User user = userWithEmail("ana@mail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertSame(user, userService.findById(1L));
    }

    @Test
    void findById_throwsUserNotFound_whenItDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(99L));
    }

    @Test
    void findAll_returnsAllUsers() {
        List<User> users = List.of(userWithEmail("a@mail.com"), userWithEmail("b@mail.com"));
        when(userRepository.findAll()).thenReturn(users);

        assertEquals(2, userService.findAll().size());
    }
}