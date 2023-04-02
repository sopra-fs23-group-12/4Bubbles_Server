package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class GameRoomController {
    private final GameRoomService gameRoomService;
    private final DTOMapper dtoMapper;

    @Autowired
    private final RoomCoordinator roomCoordinator;


    GameRoomController(UserService userService, GameRoomService gameRoomService, DTOMapper dtoMapper, RoomCoordinator roomCoordinator) {
        this.gameRoomService = gameRoomService;
        this.dtoMapper = dtoMapper;
        this.roomCoordinator = roomCoordinator;
    }

    //update with barerToken
    @PostMapping("/createRoom")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO createGameRoom(@RequestBody GameRoomPostDTO gameRoomPostDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        //convert DTO with only GameRoom object containing userId to contain user instance
        GameRoom gameRoomInput = dtoMapper.convertGameRoomPostDTOtoEntity(gameRoomPostDTO);
        //set RoomCode and add Leader to members list
        gameRoomService.initGameRoom(gameRoomInput);
        //add room to coordinator
        roomCoordinator.addRoom(gameRoomInput);
        //return DTO containing leader as user instance
        return dtoMapper.convertEntityToGameRoomGetDTO(gameRoomInput);
    }

    @PutMapping("/joinRoom/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO joinGameRoom(@RequestBody GameRoomPutDTO gameRoomPutDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        //take userId and gameRoom code
        //GameRoom gameRoomInput = dtoMapper.convertGameRoomPutDTOtoEntity(gameRoomPutDTO);
        // get gameRoom
        GameRoom room = roomCoordinator.getRoomByCode(gameRoomPutDTO.getRoomCode());
        //intialize gameRoom
        gameRoomService.addPlayerToGameRoom(room, gameRoomPutDTO.getUserId());
        //add current user to members
        //createdGameRoom.addPlayer(gameRoomPostDTO.getLeader(), bearerToken);
        return dtoMapper.convertEntityToGameRoomGetDTO(room);
    }

    /*@PutMapping("/joinRoom/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO joinGameRoom(@RequestBody GameRoomPutDTO gameRoomPutDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken {
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
}
