package ch.uzh.ifi.hase.soprafs23.Game;


import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class GameTest {

    private GameRoom gameRoom;
    private Game game;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setPassword("password1");
        testUser1.setUsername("playerName1");

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("password2");
        testUser2.setUsername("playerName2");

        /*User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testUsername2");
        user2.setToken("2");
        user2.setStatus(UserStatus.ONLINE);*/

        List<User> users = List.of(testUser1, testUser2);


        gameRoom = new GameRoom();
        gameRoom.setMembers(users);
        gameRoom.setLeader(testUser1);

        game = new Game(gameRoom);


        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        //Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void testGameRound() {
        game.startGame();



        /*// when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());*/
    }
}
