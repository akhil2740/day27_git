package com.example.UserService.controller;

import com.example.UserService.entity.User;
import com.example.UserService.exceptions.UserAlreadyExistsException;
import com.example.UserService.exceptions.UserNotFoundException;
import com.example.UserService.service.UserService;
//import com.example.UserService.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    
    private long userId;

    @Test
    void testCreateUser() {
        // Arrange
        User mockUser = new User(1L, "John Doe", "john@example.com", "password");
        when(userService.addUser(any(User.class))).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = userController.createUser(new User());

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testGetSingleUser() {
        // Arrange
        long userId = 1L;
        User mockUser = new User(userId, "John Doe", "john@example.com", "password");
        when(userService.getUserById(userId)).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = userController.getSingleUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetAllUser() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User(1L, "John Doe", "john@example.com", "password"),
                new User(2L, "Jane Doe", "jane@example.com", "password")
        );
        when(userService.getAllUser()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(userService, times(1)).getAllUser();
    }

    @Test
    void testUpdateUser() {
        // Arrange
    
        User mockUser = new User(userId, "John Doe", "john@example.com", "password");
        when(userService.updateUser(any(User.class),userId)).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = userController.updateUser(new User(),userId );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).updateUser(any(User.class), userId);
    }

    @Test
    void testUserNotFoundException() {
        // Arrange
        long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<User> response = userController.getSingleUser(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       // assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    void testUserAlreadyExistsException() {
        // Arrange
        when(userService.addUser(any(User.class))).thenThrow(new UserAlreadyExistsException("User already exists"));

        // Act
        ResponseEntity<User> response = userController.createUser(new User());

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        //assertEquals("User already exists", response.getBody().getMessage());
    }
}
