package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Qualifier;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ComponentScan("ch.uzh.ifi.hase.soprafs23.repository")
public class UserServiceUnitTests {

    private User testUser1;
    private User testUser2;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("playerName1");
        testUser1.setPassword("password1");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("playerName2");
        testUser2.setPassword("password2");
    }

    @Test
    public void testGetUsers_ReturnsListWithContent() throws Exception {
        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findAll()).thenReturn(List.of(testUser1, testUser2));
        List<User> users = userService.getUsers("mockBearer");
        Assertions.assertTrue(users.size()>0);
    }

    @Test
    public void testGetUsers_NotLoggedIn_ThrowsResponseStatusExceptionForbidden() throws Exception {
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.getUsers("mockBearer");
        });
        String baseErrorMessage = "\"You need to log in to see this information.\"";
        Assertions.assertEquals(responseStatusException.getMessage(), HttpStatus.FORBIDDEN + " " +
                String.format(baseErrorMessage));
    }

    @Test
    public void testGetUser_ReturnUser() throws Exception {
        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        User user = userService.getUser(1L, "bearerToken");
        Assertions.assertTrue(user != null);
    }

    @Test
    public void testGetUser_UserAtIndexDoesNotExist() throws Exception {
        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.getUser(1L, "bearerToken");
        });
        String baseErrorMessage = "\"The user with id %s was not found.\"";
        Assertions.assertEquals(responseStatusException.getMessage(),HttpStatus.NOT_FOUND +" " + String.format(baseErrorMessage, 1L));
    }

    @Test
    public void testUpdateUser_UserNameSuccessfullyChanged() throws Exception {
        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        Mockito.when(userRepository.save(testUser2)).thenReturn(testUser2);


        User user = userService.updateUser(1L, "bearerToken", testUser2);
        Assertions.assertTrue(testUser1.getUsername().equals(testUser2.getUsername()));

        testUser1.setUsername("playerName1");
        testUser1.setPassword("password1");

        testUser2.setUsername("playerName2");
        testUser2.setPassword("password2");
    }

    @Test
    public void testUpdateUser_NoUserNameAndBirthDateSet() throws Exception {
        testUser2.setUsername(null);

        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        Mockito.when(userRepository.save(testUser2)).thenReturn(testUser2);

        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.updateUser(1L, "bearerToken", testUser2);
        });

        testUser2.setUsername("playerName2");
    }

    @Test
    public void testUpdateUser_NewUserNameEmpty() throws Exception {
        testUser2.setUsername("");

        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        Mockito.when(userRepository.save(testUser2)).thenReturn(testUser2);

        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.updateUser(1L, "bearerToken", testUser2);
        });

        testUser2.setUsername("playerName2");
    }

    @Test
    public void testUpdateUserStats_UserNameSuccessfullyChanged() throws Exception {
        Mockito.when(userRepository.findByToken(ArgumentMatchers.any())).thenReturn(testUser1);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        Mockito.when(userRepository.save(testUser2)).thenReturn(testUser2);


        User user = userService.updateUser(1L, "bearerToken", testUser2);
        Assertions.assertTrue(testUser1.getUsername().equals(testUser2.getUsername()));

        testUser1.setUsername("playerName1");
        testUser1.setPassword("password1");

        testUser2.setUsername("playerName2");
        testUser2.setPassword("password2");
    }

    @Test
    public void testCreateUser_UserSuccessfullyCreated() throws Exception {
        Mockito.when(userRepository.save(testUser2)).thenReturn(testUser2);

        User user = userService.createUser(testUser2);
        Assertions.assertTrue(user.getToken() != null && user.getStatus() == UserStatus.OFFLINE);
    }

    @Test
    public void testRegisterUser_UserSuccessfullyCreated() throws Exception {
        Mockito.when(userRepository.save(testUser2)).thenReturn(testUser2);

        User user = userService.registerUser(testUser2);
        Assertions.assertNotEquals(user.getToken(), null);
        Assertions.assertEquals(user.getStatus(), UserStatus.ONLINE);
    }
}
