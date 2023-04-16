package ch.uzh.ifi.hase.soprafs23;


//class to control websockets
//from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d

import ch.uzh.ifi.hase.soprafs23.Game.Game;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


/*
    server.addConnectListener(onConnected()) => triggering when someone connect to the socket
    server.addDisconnectListener(onDisconnected()) => triggering when someone disconnect from the socket
    server.addEventListener(“send_message”, Message.class, onChatReceived()) => it’s corresponding socket.on(“send_message”), which means you can handle events by given event name and object class.
    senderClient.getNamespace().getBroadcastOperations().sendEvent(“get_message”, data.getMessage()) => sending data to all of the clients, includes yourself
 */

@Slf4j
@Component
public class SocketController {


    // Create a Logger debugging
    Logger logger
            = Logger.getLogger(
            SocketController.class.getName());


    private final SocketIOServer server;
    private final SocketService socketService;
    private final GameRoomService gameRoomService;

    private RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

    public SocketController(SocketIOServer server, SocketService socketService, GameRoomService gameRoomService) {
        this.server = server;
        this.socketService = socketService;
        this.gameRoomService = gameRoomService;

        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());
        server.addEventListener("start_game", Message.class, socketStartGame());
        server.addEventListener("start_timer", Message.class, startTimer());
        server.addEventListener("join_room", Message.class, joinRoom());



    }



//call this method to start the timer for the first question
    private DataListener<Message> startTimer() {
        return (senderClient, data, ackSender) -> {
            logger.info( "timer has been started:");
            logger.info(data.getRoomCode());
            socketService.timerExample(data.getRoomCode(), senderClient);
        };
    }

    private DataListener<Message> socketStartGame() {
        return (senderClient, data, ackSender) -> {
            GameRoom gameRoom = roomCoordinator.getRoomByCode(data.getRoomCode());
            logger.info( "This game was started:");
            logger.info(String.valueOf(data.getRoomCode()));
            Game game = new Game(gameRoom);
            game.startGame();
        };
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            System.out.println("message received:");
            logger.info(senderClient.getHandshakeData().getHttpHeaders().toString());
            logger.info(data.getMessage());
            logger.info(String.valueOf(data.getRoomCode()));
            socketService.sendMessage(String.valueOf(data.getRoomCode()),"get_message", senderClient, "hello this is the server");
            socketService.sendMessage(String.valueOf(data.getRoomCode()),"timer_message", senderClient, "your time has come");

        };
    }


    //this method is only called once the gameroom has been created.
    private DataListener<Message> joinRoom() {
        return (senderClient, data, ackSender) -> {

            String roomCode = data.getRoomCode();
            Long userId = Long.parseLong(data.getUserId());
            String bearerToken = data.getBearerToken();

            //if a room is specified (and exists) and passed with the url, you sign the user into the room. Otherwise, a room is created that has the name of the socket id
            try {
                socketService.joinRoom(roomCode, userId, bearerToken);
                senderClient.joinRoom(roomCode);
                logger.info("room is joined!");
                logger.info(roomCode);
                GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
                logger.info("sending room data");
                socketService.sendRoomData(roomCode, "room_is_joined", senderClient, DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoom));
                socketService.notifyMembers(roomCode,senderClient, gameRoom);
            }
            catch (Exception e) {
                logger.info("room could not be joined, either room was null or no room with that code exists");
                logger.info(e.toString());

            }
            logger.info("Socket ID[{}]  Connected to socket");
            logger.info(senderClient.getSessionId().toString());
        };
    }



    private ConnectListener onConnected() {
        return (senderClient) -> {
            String roomCode = senderClient.getHandshakeData().getSingleUrlParam("roomCode");
            if (roomCode != null){
                senderClient.joinRoom(roomCode);
            }
            roomCode = (senderClient.getSessionId().toString());
            senderClient.joinRoom(roomCode);
            logger.info("session id was made into a room");
            logger.info("Socket ID[{}]  Connected to socket");
            logger.info(roomCode);
            socketService.sendMessage(roomCode, "get_message", senderClient, String.format("single namespace: joined room: %s", roomCode));

        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            logger.info("Client[{}] - Disconnected from socket");
            logger.info(client.getSessionId().toString());
            System.out.print("\n\n DISCONNECT!! \n\n");
        };
    }

}

