package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;


@WebMvcTest(GameRoomController.class)
public class GameRoomControllerTest {

    private User testUser1;
    private User testUser2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private GameRoomService gameRoomServiceMock;

    @MockBean
    UserService userService;

    @MockBean
    private ApiService apiService;

    @MockBean
    private RoomCoordinator roomCoordinator;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("password2");
        testUser2.setUsername("playerName2");
    }

    @Test
    public void testCreateRoomStatusCode() throws Exception {
        GameRoomPostDTO gameRoomPostDTO = new GameRoomPostDTO();
        gameRoomPostDTO.setLeaderId(1L);
        gameRoomPostDTO.setQuestionTopic("Geography");
        gameRoomPostDTO.setGameMode("Basic");
        gameRoomPostDTO.setNumOfQuestions(5);
        gameRoomPostDTO.setQuestionTopicId(3);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameRoomPostDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateRoom_AuthorizationHeader() throws Exception {
        GameRoomPostDTO gameRoomPostDTO = new GameRoomPostDTO();
        gameRoomPostDTO.setLeaderId(1L);
        gameRoomPostDTO.setQuestionTopic("Geography");
        gameRoomPostDTO.setGameMode("Basic");
        gameRoomPostDTO.setNumOfQuestions(5);
        gameRoomPostDTO.setQuestionTopicId(3);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameRoomPostDTO))
                .header("Authorization","BearerToken")
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        verify(gameRoomServiceMock, times(1)).throwForbiddenWhenNoBearerToken("BearerToken");

    }

    @Test
    public void testCreateRoom_BodyDTOMapping() throws Exception {
        GameRoomPostDTO gameRoomPostDTO = new GameRoomPostDTO();
        gameRoomPostDTO.setLeaderId(1L);
        gameRoomPostDTO.setQuestionTopic("Geography");
        gameRoomPostDTO.setGameMode("Basic");
        gameRoomPostDTO.setNumOfQuestions(5);
        gameRoomPostDTO.setQuestionTopicId(3);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameRoomPostDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("gameMode", Matchers.is("Basic")));
    }

    @Test
    public void testGetTopics() throws Exception {
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



