package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameIsRunningExeption;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//this is for the websockets
// from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
@Service
@Slf4j
public class SocketService {

    private final GameRoomService gameRoomService;
    private final RoomCoordinator roomCoordinator;
    private final SocketBasics socketBasics;
    public SocketService(GameRoomService gameRoomService, RoomCoordinator roomCoordinator) {
        this.gameRoomService = gameRoomService;
        this.roomCoordinator = RoomCoordinator.getInstance();
        this.socketBasics = new SocketBasics();
    }

    public void joinRoom( String roomCode, long userId, String bearerToken, SocketIOClient senderClient){
        gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
        try {
            GameRoom room = roomCoordinator.getRoomByCode(roomCode);
            
            if (room.isGameStarted()){
                throw new GameIsRunningExeption("Game is already running");
            }
            if (room.getLeaderUserId() != userId ){ //only add the new member if the member is not the game leader (the leader is added upon creation)
                gameRoomService.addPlayerToGameRoom(room, userId);
            }
            if (room.getNamespace() == null){
                room.setNamespace(senderClient.getNamespace());
            }
        }catch (RoomNotFoundException e) {
            socketBasics.sendObject(EventNames.AN_ERROR_OCCURED.eventName, "Unable to find game room with code: " + roomCode, senderClient);
            throw new RoomNotFoundException("Unable to find game room with code: " + roomCode);
        }catch (GameIsRunningExeption e){
            socketBasics.sendObject(EventNames.AN_ERROR_OCCURED.eventName, "Unable to join the room. The game is already running", senderClient);
            throw new GameIsRunningExeption("Game is already running");
        }

    }

    //send a list of all members to all members in a gameroom
    public void sendMemberArray(String roomCode, SocketIOClient senderClient){
        try{
            GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
            Collection<User> members = gameRoom.getMembers().values();
            List<User> membersList = members.stream().toList();
            socketBasics.sendObjectToRoom(roomCode, EventNames.JOINED_PLAYERS.eventName, membersList);
        } catch (Exception e){
            throw new RoomNotFoundException("Unable to find game room with code: " + roomCode);
        }
    }
    

    //this is used to send a data package for the data hook
    public void sendObject(SocketIOClient client, String eventName, Object data) {
        socketBasics.sendObject(eventName, data, client);
    }


    public HashMap<String, Integer> votesListAsMap(Map<Long, Vote> votes) {
        HashMap<String, Integer> votesDict = new HashMap<String, Integer>();
        for(Vote entry : votes.values()) {
            Integer i = votesDict.get(entry.getVote());
            if(i == null) {votesDict.put(entry.getVote(), 1);}
            else votesDict.put(entry.getVote(), i + 1);
        }
        return votesDict;
    }

    
    public void removePlayerFromGameRoom(GameRoom room, Long userId) {
        room.getMembers().remove(userId);
    }
}




