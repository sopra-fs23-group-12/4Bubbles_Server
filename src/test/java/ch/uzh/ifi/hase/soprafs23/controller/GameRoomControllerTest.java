package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.ApiUrls;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.exceptions.ApiConnectionError;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(GameRoomController.class)
public class GameRoomControllerTest {

    private User testUser1;
    private User testUser2;

    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private GameRoomService gameRoomServiceMock;

    @MockBean
    private RoomCoordinator roomCoordinatorMock;

    @Mock
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @MockBean
    private ApiService apiService;

    /*@InjectMocks
    private GameRoomController gameRoomController = new GameRoomController(userService, gameRoomServiceMock, roomCoordinatorMock, apiService);
*/
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(gameRoomController).build();
        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("password2");
        testUser2.setUsername("playerName2");
    }

    @Test
    public void testJoinRoom() throws Exception {

        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setPassword("password1");
        testUser1.setUsername("playerName1");

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");
        gameRoom.setLeader(testUser1);
        gameRoom.setMembers(List.of(testUser1));

        GameRoomPutDTO gameRoomPutDTO = new GameRoomPutDTO();
        gameRoomPutDTO.setRoomCode("123456");
        gameRoomPutDTO.setUserId(2);

        RoomCoordinator roomCoordinatorSpy = spy(RoomCoordinator.getInstance());

        roomCoordinatorSpy.addRoom(gameRoom);

        /*GameRoomService gameRoomServiceMock = spy(new GameRoomService(userRepository));

        doReturn(testUser2).when(gameRoomServiceMock).retrieveUserFromRepo(2L);

        verify(gameRoomServiceMock).retrieveUserFromRepo(2L);*/

        given(gameRoomServiceMock.retrieveUserFromRepo(2L)).willReturn(testUser2);
        given(roomCoordinatorSpy.getRoomByCode("123456")).willReturn(gameRoom);
        doNothing().when(gameRoomServiceMock).throwForbiddenWhenNoBearerToken(any());
        when(gameRoomServiceMock.addPlayerToGameRoom(gameRoom,2L)).thenCallRealMethod();





        mockMvc.perform(MockMvcRequestBuilders
                        .put("/joinRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(gameRoomPutDTO))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("roomCode", Matchers.is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("members[1].username", Matchers.is("playerName2")));

        //verify(gameRoomServiceMock).retrieveUserFromRepo(2L);
        //.andExpect(MockMvcResultMatchers.header().string("roomCode", Matchers.containsString("1234562")));
                /*.andExpect((ResultMatcher) jsonPath("$.roomCode", gameRoom.getRoomCode()))
                .andExpect((ResultMatcher) jsonPath("$.leader", gameRoom.getLeader()))
                .andExpect(jsonPath("$.members", hasSize(1)));*/
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}

    /*@Test
    public void testCreateRoom() throws Exception {
        //Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable((testUser1)));
        //this.gameRoomService =  new GameRoomService(userRepository);

        doNothing().when(gameRoomService).throwForbiddenWhenNoBearerToken(Mockito.any());
        doNothing().when(gameRoomService).setLeaderFromRepo(Mockito.any());
        doNothing().when(gameRoomService).initGameRoom(Mockito.any());
        given(apiService.getQuestionsFromApi(Mockito.any())).willReturn(Collections.emptyList());
        doNothing().when(roomCoordinator).addRoom(Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameRoomPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
                *//*.andExpect(jsonPath("$.roomCode", is(String.class)))
                .andExpect((ResultMatcher) jsonPath("$.leader", gameRoom.getLeader()))
                .andExpect(jsonPath("$.members", hasSize(1)));*//*
    }*/

    /*gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
    GameRoom gameRoom = DTOMapper.INSTANCE.convertGameRoomPostDTOtoEntity(gameRoomPostDTO);
        gameRoomService.setLeaderFromRepo(gameRoom);
        gameRoomService.initGameRoom(gameRoom);
    String apiURL = String.format(ApiUrls.QUESTIONS.url, gameRoom.getNumOfQuestions(), gameRoom.getQuestionTopicId(), gameRoom.getGameMode());
        try{
        gameRoom.setQuestions(apiService.getQuestionsFromApi(apiURL));
        gameRoom.getQuestions().forEach(question -> System.out.println(question.getQuestion()));
    } catch (
    IOException e) {
        throw new ApiConnectionError("Something went wrong while accessing the API", e);
    }
        roomCoordinator.addRoom(gameRoom);

        return DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoom);
     */

    //setup GameRoom
    /*testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setPassword("password1");
        testUser1.setUsername("playerName1");

    GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");
        gameRoom.setLeader(testUser1);
        gameRoom.setMembers(List.of(testUser1));

    GameRoomPostDTO gameRoomPostDTO = new GameRoomPostDTO();
        gameRoomPostDTO.setLeaderId(1L);
        gameRoomPostDTO.setQuestionTopic("Geography");
        gameRoomPostDTO.setGameMode("Basic");
        gameRoomPostDTO.setNumOfQuestions(5);
        gameRoomPostDTO.setQuestionTopicId(3);*/

