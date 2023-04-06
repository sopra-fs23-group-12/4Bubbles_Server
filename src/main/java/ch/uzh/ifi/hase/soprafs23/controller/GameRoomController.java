package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class GameRoomController {
    private final GameRoomService gameRoomService;
    private final RoomCoordinator roomCoordinator;


    GameRoomController(UserService userService, GameRoomService gameRoomService, RoomCoordinator roomCoordinator) {
        this.gameRoomService = gameRoomService;
        this.roomCoordinator = roomCoordinator;
    }

    @PostMapping("/createRoom")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO createGameRoom(@RequestBody GameRoomPostDTO gameRoomPostDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        throwForbiddenWhenNoBearerToken(bearerToken);
        GameRoom gameRoomInput = DTOMapper.INSTANCE.convertGameRoomPostDTOtoEntity(gameRoomPostDTO);
        gameRoomService.initGameRoom(gameRoomInput);
        roomCoordinator.addRoom(gameRoomInput);
        return DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoomInput);
    }

    //put request has no return value -> get request
    @GetMapping("/joinRoom")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoomGetDTO joinGameRoom(@RequestBody GameRoomPutDTO GameRoomPutDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        throwForbiddenWhenNoBearerToken(bearerToken);
        try {
            GameRoom room = roomCoordinator.getRoomByCode(GameRoomPutDTO.getRoomCode());
            gameRoomService.addPlayerToGameRoom(room, GameRoomPutDTO.getUserId());
            return DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(room);
        }
        catch (NotFoundException e) {
            throw new RoomNotFoundException("Unable to find game room with code: " + GameRoomPutDTO.getRoomCode(), e);
        }
    }

    public void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }
}
