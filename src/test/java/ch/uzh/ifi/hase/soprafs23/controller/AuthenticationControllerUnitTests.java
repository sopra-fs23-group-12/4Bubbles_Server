package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.service.AuthenticationService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
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

public class AuthenticationControllerUnitTests {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private UserPostDTO userPostDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userPostDTO = new UserPostDTO();
        userPostDTO.setToken("token");

    }

    @Test
    public void testLogin() throws Exception {
        authenticationController.login(userPostDTO);
        verify(authenticationService, times(1)).authenticateUser(any(User.class));
    }

    @Test
    public void testRegister() throws Exception {
        userPostDTO.setUsername("userName");
        userPostDTO.setPassword("pw");
        authenticationController.register(userPostDTO);
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testRegister_PassWordNotSet() throws Exception {
        userPostDTO.setUsername("userName");
        userPostDTO.setPassword(null);
        assertThrows(ResponseStatusException.class, () -> {
            authenticationController.register(userPostDTO);
        });
    }

    @Test
    public void testRegister_UserNameNotSet() throws Exception {
        userPostDTO.setUsername(null);
        userPostDTO.setPassword("pw");
        assertThrows(ResponseStatusException.class, () -> {
            authenticationController.register(userPostDTO);
        });
        
    }

    @Test
    public void testRegister_BothSet() throws Exception {
        userPostDTO.setUsername("userName");
        userPostDTO.setPassword("pw");
        assertDoesNotThrow(() ->authenticationController.register(userPostDTO));
    }

    @Test
    public void testLogout() throws Exception {
        authenticationController.logout("bearer");
        verify(authenticationService, times(1)).logout(any(String.class));
    }

    //todo test bearer conversion

    @Test
    public void testLogout_NoBearerToken() throws Exception {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            authenticationController.logout(null);
        });
        String baseErrorMessage = " \"You need to log in to see this information.\"";
        assertEquals((HttpStatus.FORBIDDEN + String.format(baseErrorMessage)), responseStatusException.getMessage());
    }
}
