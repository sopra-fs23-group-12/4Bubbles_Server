package ch.uzh.ifi.hase.soprafs23.controllerSelection;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.controller.UserController;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPointsPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Date;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        BDDMockito.given(userService.getUsers(Mockito.any())).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "top-secret-token");

        // then
        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", Matchers.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(user.getStatus().toString())));
    }

    @Test
    public void getUsers_NotAuthenticated() throws Exception {

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setCreationDate(new Date());

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testUsername");

        BDDMockito.given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(user.getStatus().toString())));

    }

    @Test
    public void createUser_invalidInput() throws Exception {
        // given
        UserPostDTO userPostDTO = new UserPostDTO();

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createUser_userAlreadyExists() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testUsername");

        BDDMockito.given(userService.createUser(Mockito.any()))
                .willThrow(new ResponseStatusException(HttpStatus.CONFLICT,
                        String.format("You need to log in to see this information.")));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }

    @Test
    public void getUserById_Authenticated() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setCreationDate(new Date());

        BDDMockito.given(userService.getUser(Mockito.any(), Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users/1").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "top-secret-token");

        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        // Locale.ITALY);

        mockMvc.perform(getRequest).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(user.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", Matchers.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(user.getStatus().toString())));
        // .andExpect(jsonPath("$[0].creationDate",
        // is(format.format(user.getCreationDate()))));

    }

    @Test
    public void getUserById_NotAuthenticated() throws Exception {

        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);

        BDDMockito.given(userService.getUser(Mockito.any(), Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    public void getUserById_NotFound() throws Exception {

        BDDMockito.given(userService.getUser(Mockito.any(), Mockito
                .any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("This User does not exist.")));

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users/2").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "top-secret-token");

        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void RegisterTest() throws Exception {

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testUsername");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testUsername");

        BDDMockito.given(userService.registerUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void UpdateUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername2");

        BDDMockito.given(userService.updateUser(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO)).header("Authorization", "Bearer " + "top-secret-token");

        // then
        mockMvc.perform(postRequest)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void UpdateUser_NotAuthenticated() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername2");

        BDDMockito.given(userService.updateUser(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    public void updateUserStatsTest() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPointsPutDTO pointsPutDTO = new UserPointsPutDTO();
        pointsPutDTO.setPoints(100);
        pointsPutDTO.setId(1L);

        BDDMockito.given(userService.updateUserStats(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/users/Statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pointsPutDTO)).header("Authorization", "Bearer " + "top-secret-token");

        // then
        
                mockMvc.perform(putRequest)
                        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getStatsTest() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.increaseTotalPoints(100);
        user.increaseTotalGamesPlayed();

        BDDMockito.given(userService.getUser(Mockito.any(), Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users/Statistics/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "top-secret-token");

        // then
        
        mockMvc.perform(getRequest).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("username", Matchers.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("totalPoints", Matchers.is(user.getTotalPoints())))
                .andExpect(MockMvcResultMatchers.jsonPath("totalGamesPlayed", Matchers.is(user.getTotalGamesPlayed())));
        

    }

    @Test
    void getStats_Not_Authenticated() throws Exception{
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);

        BDDMockito.given(userService.getUser(Mockito.any(), Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/users/Statistics/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
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