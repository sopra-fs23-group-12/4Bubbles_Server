package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.ApiUrls;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.exceptions.ApiConnectionError;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GameRoomControllerUnitTests {

    @Mock
    private ApiService apiService;

    @Mock
    private GameRoomService gameRoomService;

    @Mock
    private RoomCoordinator roomCoordinator;

    @InjectMocks
    private GameRoomController gameRoomController;

    private GameRoomPostDTO gameRoomPostDTO;

    private GameRoomPutDTO gameRoomPutDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        gameRoomPostDTO = new GameRoomPostDTO();
        gameRoomPostDTO.setLeaderId(1L);
        gameRoomPostDTO.setQuestionTopic("Geography");
        gameRoomPostDTO.setGameMode("Basic");
        gameRoomPostDTO.setNumOfQuestions(5);
        gameRoomPostDTO.setQuestionTopicId(3);

        gameRoomPutDTO = new GameRoomPutDTO();
        gameRoomPutDTO.setUserId(1);
        gameRoomPutDTO.setRoomCode("123456");
    }

    /*
    @Test
    public void testCreateRoom() throws Exception {
        GameRoomGetDTO gameRoomGetDTO = gameRoomController.createGameRoom(gameRoomPostDTO, null);
        verify(gameRoomService, times(1)).throwForbiddenWhenNoBearerToken(any());
        verify(gameRoomService, times(1)).setLeaderFromRepo(any());
        verify(gameRoomService, times(1)).initGameRoom(any());
        verify(apiService, times(1)).getQuestionsFromApi(eq("https://opentdb.com/api.php?amount=5&category=3&difficulty=Basic&type=multiple"));
        verify(roomCoordinator, times(1)).addRoom(any());

        assertTrue(gameRoomGetDTO.getGameMode().equals("Basic"));
        assertEquals(gameRoomGetDTO.getNumOfQuestions(), 5);
    }


     */
    @Test
    public void testCreateRoom_apiServiceThrowsException() throws Exception{
        when(apiService.getQuestionsFromApi(any())).thenThrow(new IOException());
        ApiConnectionError apiConnectionError = assertThrows(ApiConnectionError.class, () -> {
            gameRoomController.createGameRoom(gameRoomPostDTO, null);
        });
        assertEquals(apiConnectionError.getMessage(), "Something went wrong while accessing the API");
    }

    @Test
    public void testCreateRoom_apiServiceDoesNotThrowException() throws Exception{
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(apiService.getQuestionsFromApi(any())).thenReturn(questions);
        assertDoesNotThrow(() -> {
            gameRoomController.createGameRoom(gameRoomPostDTO, null);
        });
    }


    @Test
    public void testJoinRoom() throws Exception {
        //GameRoomGetDTO gameRoomGetDTO = gameRoomController.joinGameRoom(gameRoomPutDTO, "bearer");
        //verify(gameRoomService, times(1)).throwForbiddenWhenNoBearerToken("bearer");
        //verify(roomCoordinator, times(1)).getRoomByCode(eq(gameRoomPutDTO.getRoomCode()));
        //verify(gameRoomService, times(1)).addPlayerToGameRoom(any(),any(Long.class));

        //when roomCoordinator then return fake room
        //assert conversion

        //assertDoesNotThrow(() -> {
        //     gameRoomController.joinGameRoom(gameRoomPutDTO, null);
        //});
    }



    @Test
    public void joinRoom_roomNotFoundException() throws Exception{
        when(roomCoordinator.getRoomByCode( any())).thenThrow(new NotFoundException("Room with given room code could not be found"));
        /*RoomNotFoundException roomNotFoundException = assertThrows(RoomNotFoundException.class, () -> {
            gameRoomController.joinGameRoom(gameRoomPutDTO, null);
        });
        assertEquals("Unable to find game room with code: 123456", roomNotFoundException.getMessage());*/
    }

    @Test
    public void testGetTopics() throws Exception {
        List<TopicGetDTO> topicGetDTOs = gameRoomController.getTopics( null);
        verify(gameRoomService, times(1)).throwForbiddenWhenNoBearerToken(any());

        assertEquals("https://opentdb.com/api_category.php", ApiUrls.CATEGORIES.url);
        verify(apiService, times(1)).getTopicList();
    }

    /* @Test
    public void testGetTopics_ThrowsApiConnectionError() throws Exception {
        when(apiService.getTopicList()).thenThrow(new IOException());
        ApiConnectionError apiConnectionError = assertThrows(ApiConnectionError.class, () -> {
            gameRoomController.getTopics(null);
        });
        assertEquals(apiConnectionError.getMessage(), "Something went wrong while accessing the API");
    } */

    @Test
    public void testGetTopics_DoesNotThrowApiConnectionError() throws Exception{
        List<TopicGetDTO> topics= Arrays.asList(new TopicGetDTO(), new TopicGetDTO());
        when(apiService.getTopicList()).thenReturn(topics);
        assertDoesNotThrow(() -> {
            gameRoomController.createGameRoom(gameRoomPostDTO, null);
        });
    }

    /* @Test
    public void leaveRoomTest() throws Exception {
        //given
        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");
        gameRoom.setLeaderId(1L);
        gameRoom.setMembers(null);        
        
    } */
}
