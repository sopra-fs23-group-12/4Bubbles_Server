package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameRoomTest {

    private User testUser3;
    private User testUser2;
    private User testUser1;
    private GameRoom gameRoom;

    @Mock
    private UserRepository userRepository;

    private GameRoomService roomService;
    private List<User> users;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        //setup GameRoom
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setPassword("password1");
        testUser1.setUsername("playerName1");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("password2");
        testUser2.setUsername("playerName2");


        testUser3 = new User();
        testUser3.setId(3L);
        testUser3.setPassword("password3");
        testUser3.setUsername("testUsername2");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.findByid(1L)).thenReturn(testUser1);
        Mockito.when(userRepository.findByid(2L)).thenReturn(testUser2);
        Mockito.when(userRepository.findByid(3L)).thenReturn(testUser3);

        this.roomService =  new GameRoomService(userRepository);
    }

    @Test
    public void testCreateRoom() {
        /*Mockito.when(userRepository.findById(1L)).thenReturn(testUser1);
        Mockito.when(userRepository.findById(2L)).thenReturn(testUser2);
        Mockito.when(userRepository.findById(3L)).thenReturn(testUser3);

        this.roomService =  new GameRoomService(userRepository);

        //create POSTDTO
        GameRoomPostDTO postDTO = new GameRoomPostDTO();
        postDTO.setleaderId(1L);

        GameRoom gameRoomInput = DTOMapper.INSTANCE.convertGameRoomPostDTOtoEntity(postDTO);
        this.roomService.setLeaderFromRepo(gameRoomInput);
        this.roomService.initGameRoom(gameRoomInput);
        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();
        roomCoordinator.addRoom(gameRoomInput);

        assertEquals(1, gameRoom.getMembers().size());*/
    }
}


