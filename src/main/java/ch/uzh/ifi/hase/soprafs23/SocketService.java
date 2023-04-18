package ch.uzh.ifi.hase.soprafs23;


import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import com.corundumstudio.socketio.SocketIOClient;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

//this is for the websockets
// from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
@Service
@Slf4j
public class SocketService {

    private final GameRoomService gameRoomService;
    private final RoomCoordinator roomCoordinator;

    public SocketService(GameRoomService gameRoomService, RoomCoordinator roomCoordinator) {
        this.gameRoomService = gameRoomService;
        this.roomCoordinator = RoomCoordinator.getInstance();
    }

    public void joinRoom( String roomCode, long userId, String bearerToken){
        gameRoomService.throwForbiddenWhenNoBearerToken(bearerToken);
        try {
            GameRoom room = roomCoordinator.getRoomByCode(roomCode);
            if (room.getLeaderUserId() != userId ){ //only add the new member if the member is not the game leader (the leader is added upon creation)
                gameRoomService.addPlayerToGameRoom(room, userId);
            }
        }
        catch (NotFoundException e) {
            throw new RoomNotFoundException("Unable to find game room with code: " + roomCode, e);
        }
    }

    //send a list of all members to all members in a gameroom
    public void sendMemberArray(String roomCode, SocketIOClient senderClient){
        try{
            GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
            List<User> members = gameRoom.getMembers();
            for ( SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomCode).getClients()) {
                client.sendEvent("joined_players", members);
            }
        } catch (Exception e){
            System.out.printf("Exception occurred while sending user array: %s", e);
        }

        }


    // i got the dependency from here:  https://mvnrepository.com/artifact/com.corundumstudio.socketio/netty-socketio/1.5.0
    public void sendMessage(String roomCode, String eventName, SocketIOClient senderClient, String message) {
        for ( SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomCode).getClients()) {
            //this is the version that would send to all members of a room except the sender
            //if (!client.getSessionId().equals(senderClient.getSessionId())) {
             //   client.sendEvent(eventName,
             //           new Message(MessageType.SERVER, message));
            client.sendEvent(eventName, new Message(MessageType.SERVER, message));
            }
        }

        //this is used to send a data package for the data hook
    public void sendRoomData(String roomCode, String eventName, SocketIOClient senderClient, Object data) {
        for ( SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomCode).getClients()) {
            //this is the version that would send to all members of a room except the sender
            //if (!client.getSessionId().equals(senderClient.getSessionId())) {
            //   client.sendEvent(eventName,
            //           new Message(MessageType.SERVER, message));
            client.sendEvent(eventName,  data);
        }
    }


    public void timerExample(String room, SocketIOClient senderClient ){

        int counter = 40;

        while(counter>0){
            try {
                Thread.sleep(1000);
                System.out.println(counter);

                for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()){
                    client.sendEvent("timer_count", new Message(MessageType.SERVER, String.valueOf(counter)));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter--;
        }
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()){
            client.sendEvent("timer_count", new Message(MessageType.SERVER, "time over"));
        }        }
    }



