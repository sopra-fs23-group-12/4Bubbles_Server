package ch.uzh.ifi.hase.soprafs23;


//class to control websockets
//from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d

import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
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

    private RoomCoordinator roomCoordinator = new RoomCoordinator();

    public SocketController(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;

        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());
        server.addEventListener("start_game", Message.class, startGame());
        server.addEventListener("start_timer", Message.class, startTimer());


    }

//call this method to start the timer for the first question
    private DataListener<Message> startTimer() {
        return (senderClient, data, ackSender) -> {
            logger.info( "timer has been started:");
            logger.info(data.getRoom());
            socketService.timerExample(data.getRoom(), senderClient);
        };
    }

    //call this method with the room code
    private DataListener<Message> startGame() {
        return (senderClient, data, ackSender) -> {
            roomCoordinator.getRoomByCode(Integer.parseInt(data.getRoom()));
            logger.info( "This game was started:");
            logger.info(String.valueOf(data.getRoom()));
        };
        //TODO call the start game method on the game with the provided room code;
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            System.out.println("message received:");
            logger.info(senderClient.getHandshakeData().getHttpHeaders().toString());
            logger.info(data.getMessage());
            logger.info(String.valueOf(data.getRoom()));
            socketService.sendMessage(String.valueOf(data.getRoom()),"get_message", senderClient, "hello this is the server");
            socketService.sendMessage(String.valueOf(data.getRoom()),"timer_message", senderClient, "your time has come");

        };
    }



    private ConnectListener onConnected() {
        return (client) -> {

            String room = client.getHandshakeData().getSingleUrlParam("room");

            //if a room is specified and passed with the url, you sign the user into the room. Otherwise, a room is created that has the name of the socket id
            if (room != null){
                client.joinRoom(room);
                logger.info("room is joined!");
                logger.info(room);
                socketService.sendMessage(room,"get_message", client, String.format("you have joined room: %s", room));

            }
            else {
                client.joinRoom((client.getSessionId().toString()));
                logger.info("no room available, session id was made into a room");
            }
            logger.info("Socket ID[{}]  Connected to socket");
            logger.info(client.getSessionId().toString());
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

