package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

        userService.createUser(testUser);
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser        
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);

        
        
    }
    
    @Test
    void retrieveUserFromRepoTest() {
        //given
        userService.createUser(testUser);

        User fetchedUser = gameRoomService.retrieveUserFromRepo(testUser.getId());


        assertEquals(testUser, fetchedUser);
    }

    @Test
    void initGameRoomTest() {
        GameRoom testRoom = new GameRoom();
        testRoom.setLeader(testUser);
        List<User> testList = new ArrayList<>();
        testList.add(testUser);
        gameRoomService.initGameRoom(testRoom);

        assertTrue(testRoom.getRoomCode() != null);
        assertEquals(testRoom.getMembers(), testList);

    }

    @Test
    void setLeaderFromRepoSuccessTest() {
        GameRoom testRoom = new GameRoom();
        testRoom.setLeaderUserId(testUser.getId());
        gameRoomService.setLeaderFromRepo(testRoom);
        Mockito.when(userRepository.findByid(Mockito.any())).thenReturn(testUser);

        assertTrue(testRoom.getLeader().equals(testUser));
    }


    @Test
    void addPlayerToGameRoomTest() {
        GameRoom testRoom = new GameRoom();
        List<User> memberList = new ArrayList<>();
        List<User> memberList2 = new ArrayList<>();
        testRoom.setMembers(memberList2);
        memberList.add(testUser);
        
        gameRoomService.addPlayerToGameRoom(testRoom, testUser.getId());

        assertEquals(memberList, testRoom.getMembers());
    }

    @Test
    void throwForbiddenWhenNoBearerTokenTest() {
        String testToken = null;

        assertThrows(ResponseStatusException.class, () -> gameRoomService.throwForbiddenWhenNoBearerToken(testToken));
    }
}
