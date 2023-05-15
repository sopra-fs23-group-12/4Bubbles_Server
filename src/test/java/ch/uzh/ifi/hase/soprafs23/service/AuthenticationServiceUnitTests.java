package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("playerName");
    }

    @Test
    public void testAuthenticateUser_UserExistsAndCorrectPassword() throws Exception {
        when(userRepository.findByUsername(any(String.class))).thenReturn(testUser);
        User user = authenticationService.authenticateUser(testUser);
        verify(userRepository, times(1)).findByUsername(testUser.getUsername());

        assertEquals(UserStatus.ONLINE, user.getStatus());
    }

    @Test
    public void testAuthenticateUser_UserNotExists() throws Exception {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            authenticationService.authenticateUser(testUser);
        });
        String baseErrorMessage = "\"Wrong credentials!\"";
        assertEquals(responseStatusException.getMessage(), HttpStatus.UNAUTHORIZED + " " +
                String.format(baseErrorMessage, "requested user"));
    }

    @Test
    public void testAuthenticateUser_UserExistsButWrongPassword() throws Exception {
        User testUser2 = new User();
        testUser2.setPassword("pw2");

        when(userRepository.findByUsername(any(String.class))).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () -> {
            authenticationService.authenticateUser(testUser2);
        });
        /*String baseErrorMessage = "Wrong credentials!";
        assertEquals(responseStatusException.getMessage(), HttpStatus.UNAUTHORIZED,
                String.format(baseErrorMessage, "requested user"));*/
    }

    @Test
    public void testLogout() throws Exception {
        when(userRepository.findByToken(any())).thenReturn(testUser);

        authenticationService.logout(any(String.class));
        verify(userRepository, times(1)).findByToken(testUser.getToken());

        assertEquals(UserStatus.OFFLINE, testUser.getStatus());
    }




}
