package ch.uzh.ifi.hase.soprafs23;


//class to control websockets
//from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d

import ch.uzh.ifi.hase.soprafs23.entity.Message;
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
public class SocketModule {


    // Create a Logger
    Logger logger
            = Logger.getLogger(
            SocketModule.class.getName());


    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;

        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());

        server.addEventListener("send_message", Message.class, onChatReceived());

    }


    private DataListener<Message> onChatReceived() {
        System.out.print("onchatreceived method reached");
        return (senderClient, data, ackSender) -> {

            System.out.println("message received:");
            logger.info(data.getMessage());
            logger.info(data.getRoom());
            socketService.sendMessage(data.getRoom(),"get_message", senderClient, data.getMessage());
        };
    }



    private ConnectListener onConnected() {
        return (client) -> {

            String room = client.getHandshakeData().getSingleUrlParam("room");
            System.out.printf("room: %s", room);
            logger.info("the room is:");
            logger.info(room);
            //if a room is specified and passed with the url, you sign the user into the room. Otherwise a room is created that has the name of the socket id
            if (room != null){
                client.joinRoom(room);
                logger.info("room is joined!");
                logger.info(room);
                client.joinRoom(room);
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
            System.out.print("\n\n\n DISCONNECT!! \n\n\n\n");
        };
    }

}

