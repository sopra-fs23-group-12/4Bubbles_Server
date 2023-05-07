package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.ApiUrls;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.exceptions.ApiConnectionError;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import javassist.NotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameRoomController {
    private final GameRoomService gameRoomService;
    private final RoomCoordinator roomCoordinator;
    private final ApiService apiService;

    GameRoomController(GameRoomService gameRoomService, RoomCoordinator roomCoordinator, ApiService apiService) {
        this.gameRoomService = gameRoomService;
        this.roomCoordinator = roomCoordinator;
        this.apiService = apiService;
    }

    //moved the endpoint to get all the question categories from the API here
    //having only one method in the API controller makes not much sense imo
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<TopicGetDTO> getTopics(@RequestHeader(value = "Authorization", required = false) String bearerToken){
        gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
        List<TopicGetDTO> topics = new ArrayList<TopicGetDTO>();
        String apiURL = ApiUrls.CATEGORIES.url;
        try {
            topics = apiService.getTopicsFromApi(apiURL);
        } catch (IOException e) {
            throw new ApiConnectionError("Something went wrong while accessing the API", e);
        }
        return topics;
    }
    
    //Added the fetch for questions from the API here, 
    //so that questions are fetched when a room is created
    @PostMapping("/createRoom") 
    @ResponseStatus(HttpStatus.CREATED)
    public GameRoomGetDTO createGameRoom(@RequestBody GameRoomPostDTO gameRoomPostDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
        GameRoom gameRoom = DTOMapper.INSTANCE.convertGameRoomPostDTOtoEntity(gameRoomPostDTO);
        gameRoomService.setLeaderFromRepo(gameRoom);
        gameRoomService.initGameRoom(gameRoom);
        String apiURL = String.format(ApiUrls.QUESTIONS.url, gameRoom.getNumOfQuestions(), gameRoom.getQuestionTopicId(), gameRoom.getGameMode());
        try{
            gameRoom.setQuestions(apiService.getQuestionsFromApi(apiURL));
            gameRoom.getQuestions().forEach(question -> System.out.println(question.getQuestion()));
        } catch (IOException e) {
            throw new ApiConnectionError("Something went wrong while accessing the API", e);
        }
        roomCoordinator.addRoom(gameRoom);

        return DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoom);
    }

    @PutMapping("/joinRoom") // who and which room, evtl authorization?
    @ResponseStatus(HttpStatus.OK)
    public GameRoomGetDTO joinGameRoom(@RequestBody GameRoomPutDTO GameRoomPutDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
        try {
            GameRoom room = roomCoordinator.getRoomByCode(GameRoomPutDTO.getRoomCode());
            gameRoomService.addPlayerToGameRoom(room, GameRoomPutDTO.getUserId());
            return DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(room);
        }
        catch (NotFoundException e) {
            throw new RoomNotFoundException("Unable to find game room with code: " + GameRoomPutDTO.getRoomCode(), e);
        }
    }
}
