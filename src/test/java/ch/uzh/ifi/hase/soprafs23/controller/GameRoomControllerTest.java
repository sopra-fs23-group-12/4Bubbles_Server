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
    private RoomCoordinator roomCoordinatorMock;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private GameRoomService gameRoomServiceMock;

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
    public void testCreateRoom() throws Exception {
        given(userRepository.findByid(1L)).willReturn(testUser1);
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(testUser1));
        gameRoomServiceMock = new GameRoomService(userRepository);

        GameRoomPostDTO gameRoomPostDTO = new GameRoomPostDTO();
        gameRoomPostDTO.setLeaderId(1L);
        gameRoomPostDTO.setQuestionTopic("Geography");
        gameRoomPostDTO.setGameMode("Basic");
        gameRoomPostDTO.setNumOfQuestions(5);
        gameRoomPostDTO.setQuestionTopicId(3);

        //doNothing().when(gameRoomServiceMock).throwForbiddenWhenNoBearerToken(Mockito.any());
        //doNothing().when(gameRoomServiceMock).setLeaderFromRepo(Mockito.any());
        //doNothing().when(gameRoomServiceMock).initGameRoom(Mockito.any());
        //given(apiService.getQuestionsFromApi(Mockito.any())).willReturn(Collections.emptyList());
        //doNothing().when(roomCoordinatorMock).addRoom(Mockito.any());

        //given(gameRoomServiceMock.retrieveUserFromRepo(1L)).willReturn(testUser2);
        //doNothing().when(gameRoomServiceMock).throwForbiddenWhenNoBearerToken(any());
        ;
        //GameRoom gameRoom = RoomCoordinator.getInstance().getRoomByCode("123456");
        //given(.setLeaderFromRepo(gameRoom)).thenCallRealMethod();
        //doCallRealMethod().when(gameRoomServiceMock).setLeaderFromRepo(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/createRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(gameRoomPostDTO))
                ).andExpect(MockMvcResultMatchers.status().isCreated());
                //.andExpect(MockMvcResultMatchers.jsonPath("roomCode", Matchers.is("123456")))
                //.andExpect(MockMvcResultMatchers.jsonPath("members[0].username", Matchers.is("playerName1")));

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

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();
        roomCoordinator.addRoom(gameRoom);

        given(gameRoomServiceMock.retrieveUserFromRepo(2L)).willReturn(testUser2);
        doNothing().when(gameRoomServiceMock).throwForbiddenWhenNoBearerToken(any());
        when(gameRoomServiceMock.addPlayerToGameRoom(gameRoom,2L)).thenCallRealMethod();


        mockMvc.perform(MockMvcRequestBuilders
                        .put("/joinRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(gameRoomPutDTO))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("roomCode", Matchers.is("123456")))
                .andExpect(MockMvcResultMatchers.jsonPath("members[1].username", Matchers.is("playerName2")));
    }

    @Test
    public void testGetTopics() throws Exception {
        doNothing().when(gameRoomServiceMock).throwForbiddenWhenNoBearerToken(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk());
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



