package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import com.corundumstudio.socketio.SocketIOClient;

public class SocketBasics {

    RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();


    //one generic method to send anything you want without having to instantiate SocketService
    public void sendObjectToRoom(String roomCode, String eventName, Object message) {
        try {
            GameRoom room = roomCoordinator.getRoomByCode(roomCode);
            for (SocketIOClient client : room.getNamespace().getRoomOperations(roomCode).getClients()) {
                client.sendEvent(eventName, message);
            }
        } catch (Exception e){
            System.out.printf("\n \n room not found in SocketBasics %s", roomCode);
        }
    }

    public void sendObject(String eventName, Object message, SocketIOClient client) {
        client.sendEvent(eventName, message);
    }

}
