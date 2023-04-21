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


@RestController
public class GameRoomController {
    private final GameRoomService gameRoomService;
    private final RoomCoordinator roomCoordinator;


    GameRoomController(UserService userService, GameRoomService gameRoomService, RoomCoordinator roomCoordinator) {
        this.gameRoomService = gameRoomService;
        this.roomCoordinator = RoomCoordinator.getInstance();
    }

    @PostMapping("/createRoom") // to create a room, we need; creator, namespace
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoomGetDTO createGameRoom(@RequestBody GameRoomPostDTO gameRoomPostDTO, @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
        GameRoom gameRoomInput = DTOMapper.INSTANCE.convertGameRoomPostDTOtoEntity(gameRoomPostDTO);
        gameRoomService.setLeaderFromRepo(gameRoomInput);
        gameRoomService.initGameRoom(gameRoomInput);
        roomCoordinator.addRoom(gameRoomInput);

        return DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoomInput);
    }

    @PutMapping("/joinRoom") // who and which room, evtl authorization?
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
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
