package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import ch.uzh.ifi.hase.soprafs23.exceptions.ApiConnectionError;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;

@RestController
public class ApiController {
    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
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
        String apiURL = "https://opentdb.com/api_category.php";
        try {
            topics = apiService.getTopicsFromApi(apiURL);
        } catch (IOException e) {
            throw new ApiConnectionError("Something went wrong while accessing the API", e);
        }
        
        return topics;
    }


    private void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }   
    
}
