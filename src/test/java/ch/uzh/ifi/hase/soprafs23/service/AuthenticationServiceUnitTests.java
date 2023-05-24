package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(testUser);
        User user = authenticationService.authenticateUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(testUser.getUsername());

        Assertions.assertEquals(UserStatus.ONLINE, user.getStatus());
    }

    @Test
    public void testAuthenticateUser_UserNotExists() throws Exception {
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(null);

        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authenticationService.authenticateUser(testUser);
        });
        String baseErrorMessage = "\"Wrong credentials!\"";
        Assertions.assertEquals(responseStatusException.getMessage(), HttpStatus.UNAUTHORIZED + " " +
                String.format(baseErrorMessage, "requested user"));
    }

    @Test
    public void testAuthenticateUser_UserExistsButWrongPassword() throws Exception {
        User testUser2 = new User();
        testUser2.setPassword("pw2");

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(testUser);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            authenticationService.authenticateUser(testUser2);
        });
        /*String baseErrorMessage = "Wrong credentials!";
        assertEquals(responseStatusException.getMessage(), HttpStatus.UNAUTHORIZED,
                String.format(baseErrorMessage, "requested user"));*/
    }

    @Test
    public void testLogout() throws Exception {
        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser);

        authenticationService.logout(ArgumentMatchers.any(String.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByToken(testUser.getToken());

        Assertions.assertEquals(UserStatus.OFFLINE, testUser.getStatus());
    }




}
