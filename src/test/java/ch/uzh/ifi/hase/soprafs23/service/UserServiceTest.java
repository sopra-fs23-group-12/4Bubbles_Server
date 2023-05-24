package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPointsPutDTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("testUsername");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        Assertions.assertEquals(testUser.getId(), createdUser.getId());
        Assertions.assertEquals(testUser.getUsername(), createdUser.getUsername());
        Assertions.assertEquals(testUser.getPassword(), createdUser.getPassword());
        Assertions.assertNotNull(createdUser.getToken());
        Assertions.assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    void updateUserStatsTest(){
        userService.createUser(testUser);
        Optional<User> testUser2 = Optional.of(testUser);

        UserPointsPutDTO userPointsPutDTO = new UserPointsPutDTO();
        userPointsPutDTO.setPoints(10);
        userPointsPutDTO.setId(1L);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(testUser2);

        User updatedUser = userService.updateUserStats(userPointsPutDTO);

        Assertions.assertEquals(updatedUser.getId(), userPointsPutDTO.getId());
        Assertions.assertEquals(updatedUser.getTotalPoints(), userPointsPutDTO.getPoints());
        Assertions.assertEquals(updatedUser.getTotalGamesPlayed(), 1);
        
    }

}
