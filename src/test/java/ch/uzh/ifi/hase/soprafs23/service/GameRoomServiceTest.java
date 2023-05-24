package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

class GameRoomServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private GameRoomService gameRoomService;
    
    private User testUser;
    
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);      
        // given
        long id = 1L;
        testUser = new User();
        testUser.setId(id);
        testUser.setPassword("password");
        testUser.setUsername("testUsername");

        Mockito.when(userRepository.findByid(ArgumentMatchers.any())).thenReturn(testUser);
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(testUser));

        userService.createUser(testUser);
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
    }
    
    @Test
    void retrieveUserFromRepoTest() {
        //given
        userService.createUser(testUser);

        User fetchedUser = gameRoomService.retrieveUserFromRepo(testUser.getId());


        Assertions.assertEquals(testUser, fetchedUser);
    }

    @Test
    void initGameRoomTest() {
        GameRoom testRoom = new GameRoom();
        testRoom.setLeader(testUser);
        Map<Long, User> testList = new HashMap<Long, User>();
        testList.put(testUser.getId(), testUser);
        gameRoomService.initGameRoom(testRoom);

        Assertions.assertNotNull(testRoom.getRoomCode());
        Assertions.assertEquals(testRoom.getRoomCode().length(), 6);
        Assertions.assertEquals(testRoom.getMembers(), testList);

    }

    @Test
    void setLeaderFromRepoTest() {
        GameRoom testRoom = new GameRoom();
        testRoom.setLeaderUserId(testUser.getId());
        gameRoomService.setLeaderFromRepo(testRoom);
        Mockito.when(userRepository.findByid(ArgumentMatchers.any())).thenReturn(testUser);

        Assertions.assertEquals(testRoom.getLeader(), testUser);
    }


    @Test
    void addPlayerToGameRoomTest() {
        GameRoom testRoom = new GameRoom();
        Map<Long, User> memberList = new HashMap<Long, User>();
        testRoom.getMembers().put(testUser.getId(), testUser);
        memberList.put(testUser.getId(), testUser);
        
        gameRoomService.addPlayerToGameRoom(testRoom, testUser.getId());

        Assertions.assertEquals(memberList, testRoom.getMembers());
    }

    @Test
    void throwForbiddenWhenNoBearerTokenTest() {
        String testToken = null;

        Assertions.assertThrows(ResponseStatusException.class, () -> gameRoomService.throwForbiddenWhenNoBearerToken(testToken));
    }
}
