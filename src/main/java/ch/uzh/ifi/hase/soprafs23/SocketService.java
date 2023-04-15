package ch.uzh.ifi.hase.soprafs23;


import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

//this is for the websockets
// from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
@Service
@Slf4j
public class SocketService {


    // i got the dependency from here:  https://mvnrepository.com/artifact/com.corundumstudio.socketio/netty-socketio/1.5.0
    public void sendMessage(String room, String eventName, SocketIOClient senderClient, String message) {
        for ( SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            //this is the version that would send to all members of a room except the sender
            //if (!client.getSessionId().equals(senderClient.getSessionId())) {
             //   client.sendEvent(eventName,
             //           new Message(MessageType.SERVER, message));
            client.sendEvent(eventName, new Message(MessageType.SERVER, message));
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



