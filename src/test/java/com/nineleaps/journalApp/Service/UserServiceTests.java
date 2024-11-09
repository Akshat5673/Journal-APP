package com.nineleaps.journalApp.Service;

import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Enums.Role;
import com.nineleaps.journalApp.Exceptions.AlreadyExistsException;
import com.nineleaps.journalApp.Exceptions.ResourceNotFoundException;
import com.nineleaps.journalApp.Repositories.UserRepository;
import com.nineleaps.journalApp.Services.Impls.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("123");
        user.setName("Test User");
        user.setPassword("password");
        user.setRole(Role.NORMAL_USER);
    }

    @Test
    void getAllUsers_Success() {
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void retrieve_UserExists() {
        when(userRepo.findById("123")).thenReturn(Optional.of(user));

        User retrievedUser = userService.retrieve("123");

        assertNotNull(retrievedUser);
        assertEquals("Test User", retrievedUser.getName());
        verify(userRepo, times(1)).findById("123");
    }

    @Test
    void retrieve_UserNotFound() {
        when(userRepo.findById("123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.retrieve("123"));

        verify(userRepo, times(1)).findById("123");
    }

    @Test
    void create_UserAlreadyExists() {
        when(userRepo.existsByName("Test User")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.create(user));

        verify(userRepo, times(1)).existsByName("Test User");
    }

    @Test
    void create_Success() {
        when(userRepo.existsByName("Test User")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getName());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(Role.ADMIN_USER, createdUser.getRole());
        verify(userRepo, times(1)).existsByName("Test User");
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void saveNewUser_Success() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.saveNewUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.NORMAL_USER, user.getRole());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void update_UserExists() {
        when(userRepo.findById("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);

        User newUser = new User();
        newUser.setName("Updated User");
        newUser.setPassword("newPassword");

        User updatedUser = userService.update("123", newUser);

        assertEquals("Updated User", updatedUser.getName());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        verify(userRepo, times(1)).findById("123");
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void update_UserNotFound() {
        when(userRepo.findById("123")).thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setName("Updated User");
        newUser.setPassword("newPassword");

        assertThrows(ResourceNotFoundException.class, () -> userService.update("123", newUser));

        verify(userRepo, times(1)).findById("123");
    }

    @Test
    void delete_UserExists() {
        when(userRepo.findById("123")).thenReturn(Optional.of(user));

        userService.delete("123");

        verify(userRepo, times(1)).findById("123");
        verify(userRepo, times(1)).delete(user);
    }

    @Test
    void delete_UserNotFound() {
        when(userRepo.findById("123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete("123"));

        verify(userRepo, times(1)).findById("123");
    }
}
