package fr.fortytwo.services;

import fr.fortytwo.exceptions.AlreadyAuthenticatedException;
import fr.fortytwo.exceptions.EntityNotFoundException;
import fr.fortytwo.models.User;
import fr.fortytwo.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceImplTest {

    private UsersRepository usersRepository;
    private UsersServiceImpl usersService;


    @BeforeEach
    void setUp() {
        usersRepository = Mockito.mock(UsersRepository.class);
        usersService = new UsersServiceImpl(usersRepository);
    }

    @Test
    @DisplayName("authenticate returns true and calls update when credentials are correct")
    void authenticate_correctCredentials_returnsTrueAndUpdatesUser() {
        // Arrange
        User user = new User(1L, "john", "secret", false);
        when(usersRepository.findByLogin("john")).thenReturn(user);

        // Act
        boolean result = usersService.authenticate("john", "secret");

        // Assert
        assertTrue(result, "authenticate() should return true for correct credentials");
        assertTrue(user.isAuthenticated(), "User authenticated flag should be set to true");

        // Verify that update was called exactly once with the updated user
        verify(usersRepository, times(1)).update(user);
    }

    @Test
    @DisplayName("authenticate propagates EntityNotFoundException for unknown login")
    void authenticate_unknownLogin_throwsEntityNotFoundException() {
        // Arrange – stub findByLogin to simulate a missing user
        when(usersRepository.findByLogin("unknown"))
                .thenThrow(new EntityNotFoundException("User 'unknown' not found"));

        // Act & Assert
        assertThrows(
                EntityNotFoundException.class,
                () -> usersService.authenticate("unknown", "anyPassword"),
                "EntityNotFoundException should propagate when login is not found");

        // update must never be called
        verify(usersRepository, never()).update(any());
    }

    @Test
    @DisplayName("authenticate returns false when password does not match")
    void authenticate_wrongPassword_returnsFalse() {
        // Arrange
        User user = new User(2L, "jane", "correctPassword", false);
        when(usersRepository.findByLogin("jane")).thenReturn(user);

        // Act
        boolean result = usersService.authenticate("jane", "wrongPassword");

        // Assert
        assertFalse(result, "authenticate() should return false for a wrong password");
        assertFalse(user.isAuthenticated(), "User authenticated flag must remain false");

        // update must never be called on a failed authentication
        verify(usersRepository, never()).update(any());
    }

    @Test
    @DisplayName("authenticate throws AlreadyAuthenticatedException when user is already authenticated")
    void authenticate_alreadyAuthenticated_throwsAlreadyAuthenticatedException() {
        // Arrange – user is already marked as authenticated
        User user = new User(3L, "bob", "pass", true);
        when(usersRepository.findByLogin("bob")).thenReturn(user);

        // Act & Assert
        assertThrows(
                AlreadyAuthenticatedException.class,
                () -> usersService.authenticate("bob", "pass"),
                "AlreadyAuthenticatedException should be thrown for an already-authenticated user");

        verify(usersRepository, never()).update(any());
    }
}
