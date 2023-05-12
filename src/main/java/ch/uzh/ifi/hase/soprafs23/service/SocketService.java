package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;

import com.corundumstudio.socketio.SocketIOClient;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
            if (room.getLeaderUserId() != userId ){ //only add the new member if the member is not the game leader (the leader is added upon creation)
                gameRoomService.addPlayerToGameRoom(room, userId);
            }
            if (room.getNamespace() == null){
                room.setNamespace(senderClient.getNamespace());
            }
        }
        catch (NotFoundException e) {
            //this will be triggered every time a client connects with their individual ID generated by Socketio, this is fine and can safely be ignored
            //the line is only relevant to catch if the room should actually exist
            throw new RoomNotFoundException("Unable to find game room with code: " + roomCode, e);
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
            System.out.printf("Exception occurred while sending user array: %s", e);
        }

    }


    // i got the dependency from here:  https://mvnrepository.com/artifact/com.corundumstudio.socketio/netty-socketio/1.5.0
    public void sendMessage(SocketIOClient client, String eventName, String message) {
        socketBasics.sendObject(eventName, message, client);
    }

    public void sendAnswers(String roomCode, SocketIOClient senderClient, String answers) {
        socketBasics.sendObjectToRoom(roomCode, EventNames.GET_ANSWERS.eventName, new Message(MessageType.SERVER, answers));
        
    }

    public void sendQuestion(String roomCode, SocketIOClient senderClient, String question) {
        socketBasics.sendObjectToRoom(roomCode, EventNames.GET_QUESTION.eventName, new Message(MessageType.SERVER, question));
    }


    //this is used to send a data package for the data hook
    public void sendObject(SocketIOClient client, String eventName, Object data) {
        socketBasics.sendObject(eventName, data, client);
    }


    public HashMap<String, Integer> votesListAsMap(List<Vote> votes) {
        HashMap<String, Integer> votesDict = new HashMap<String, Integer>();
        for(Vote entry : votes) {
            Integer i = votesDict.get(entry.getVote());
            if(i == null) {votesDict.put(entry.getVote(), 1);}
            else votesDict.put(entry.getVote(), i + 1);
        }
        return votesDict;
    }

    }



