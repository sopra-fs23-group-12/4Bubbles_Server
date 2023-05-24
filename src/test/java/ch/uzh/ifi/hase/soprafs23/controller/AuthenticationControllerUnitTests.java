package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.service.AuthenticationService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.*;
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
        LoginGetDTO loginGetDTO = authenticationController.login(userPostDTO);
        Mockito.verify(authenticationService, Mockito.times(1)).authenticateUser(ArgumentMatchers.any(User.class));
    }

    @Test
    public void testRegister() throws Exception {
        userPostDTO.setUsername("userName");
        userPostDTO.setPassword("pw");
        LoginGetDTO loginGetDTO = authenticationController.register(userPostDTO);
        Mockito.verify(userService, Mockito.times(1)).registerUser(ArgumentMatchers.any(User.class));
    }

    @Test
    public void testRegister_PassWordNotSet() throws Exception {
        userPostDTO.setUsername("userName");
        userPostDTO.setPassword(null);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authenticationController.register(userPostDTO);
        });
        //String baseErrorMessage = "Oups, your request is wrong. ";
        //assertEquals((HttpStatus.BAD_REQUEST + String.format( baseErrorMessage)), responseStatusException.getMessage());
    }

    @Test
    public void testRegister_UserNameNotSet() throws Exception {
        userPostDTO.setUsername(null);
        userPostDTO.setPassword("pw");
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authenticationController.register(userPostDTO);
        });
        //String baseErrorMessage = "Oups, your request is wrong. ";
        //assertEquals((HttpStatus.BAD_REQUEST + String.format( baseErrorMessage)), responseStatusException.getMessage());
    }

    @Test
    public void testRegister_BothSet() throws Exception {
        userPostDTO.setUsername("userName");
        userPostDTO.setPassword("pw");
        Assertions.assertDoesNotThrow(() ->authenticationController.register(userPostDTO));
    }

    @Test
    public void testLogout() throws Exception {
        //StringBuffer bearerToken = new StringBuffer("bearer");
        authenticationController.logout("bearer");
        Mockito.verify(authenticationService, Mockito.times(1)).logout(ArgumentMatchers.any(String.class));
        //assertEquals("", bearerToken);
    }

    //todo test bearer conversion

    @Test
    public void testLogout_NoBearerToken() throws Exception {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authenticationController.logout(null);
        });
        String baseErrorMessage = " \"You need to log in to see this information.\"";
        Assertions.assertEquals((HttpStatus.FORBIDDEN + String.format(baseErrorMessage)), responseStatusException.getMessage());
    }
}
