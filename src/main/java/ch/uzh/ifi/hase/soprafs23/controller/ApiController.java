package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.ApiUrls;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.exceptions.ApiConnectionError;
import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;
import javassist.NotFoundException;

@RestController
public class ApiController {
    private final ApiService apiService;
    private final RoomCoordinator roomCoordinator;

    public ApiController(ApiService apiService, RoomCoordinator roomCoordinator) {
        this.apiService = apiService;
        this.roomCoordinator = RoomCoordinator.getInstance();
    }


    //Returns a list of all available topics from the API
    //The list is then used to populate the dropdown menu in the frontend
    //API url is defined here for now, but it might makes sense to send it with a requestBody
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TopicGetDTO> getTopics(@RequestHeader(value = "Authorization", required = false) String bearerToken){
        throwForbiddenWhenNoBearerToken(bearerToken);
        List<TopicGetDTO> topics = new ArrayList<TopicGetDTO>();
        String apiURL = ApiUrls.CATEGORIES.url;
        try {
            topics = apiService.getTopicsFromApi(apiURL);
        } catch (IOException e) {
            throw new ApiConnectionError("Something went wrong while accessing the API", e);
        }
        
        return topics;
    }


    @GetMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getQuestions(@RequestParam String roomCode, @RequestHeader(value = "Authorization", required = false) String bearerToken){
        throwForbiddenWhenNoBearerToken(bearerToken);

        GameRoom room = new GameRoom();
        try {
            room = roomCoordinator.getRoomByCode(roomCode);
        } catch (NotFoundException e) {
            //Handle error better in future
            System.out.println("Room not found");
        }
        String apiURL = String.format(ApiUrls.QUESTIONS.url, room.getNumOfQuestions(), room.getQuestionTopicId());
        
        try {
            room.setQuestions(apiService.getQuestionsFromApi(apiURL));
        } catch (IOException e) {
            throw new ApiConnectionError("Something went wrong while accessing the API", e);
        }
        
        
    }

    //Helper method that throws a 403 error when no bearer token is present
    private void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }   
    
}
