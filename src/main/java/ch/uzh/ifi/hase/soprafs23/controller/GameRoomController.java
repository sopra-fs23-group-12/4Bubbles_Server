package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class GameRoomController {
    private final GameRoomService gameRoomService;
    private final DTOMapper dtoMapper;


    GameRoomController(UserService userService, GameRoomService gameRoomService, DTOMapper dtoMapper) {
        this.gameRoomService = gameRoomService;
        this.dtoMapper = dtoMapper;
    }

    //update with barerToken
    @PostMapping("/createRoom")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO createGameRoom(@RequestBody GameRoomPostDTO gameRoomPostDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        //take also userId
        GameRoom gameRoomInput = dtoMapper.convertGameRoomPostDTOtoEntity(gameRoomPostDTO);
        //intialize gameRoom
        gameRoomService.initGameRoom(gameRoomInput);
        //add current user to members
        //createdGameRoom.addPlayer(gameRoomPostDTO.getLeader(), bearerToken);
        return dtoMapper.convertEntityToGameRoomGetDTO(gameRoomInput);
    }

    /*
    @PutMapping("/joinRoom/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO createGameRoom(@RequestBody GameRoomPutDTO gameRoomPutDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken {
        //take userId and gameRoom code
        GameRoom gameRoomInput = dtoMapper.convertGameRoomPutDTOtoEntity(gameRoomPutDTO);
        //intialize gameRoom
        gameRoomService.addPlayerToGameRoom();
        //add current user to members
        //createdGameRoom.addPlayer(gameRoomPostDTO.getLeader(), bearerToken);
        return dtoMapper.convertEntityToGameRoomGetDTO(gameRoomInput);
    }*/

    public void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }

    /*
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createRoom(@RequestBody GameRoomDTO gameRoomDTO) {

        // convert API user to internal representation
        //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        //with empty fields


        new GameRoom();


        // check if password and username is set
        if (userInput.getPassword() == null || userInput.getUsername() == null) {
            String baseErrorMessage = "Oups, your request is wrong. ";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }

        // create user
        User createdUser = userService.createUser(userInput);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }
    */
}
