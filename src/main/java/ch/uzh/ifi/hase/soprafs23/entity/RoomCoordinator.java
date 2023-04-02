package ch.uzh.ifi.hase.soprafs23.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RoomCoordinator {
    ArrayList<GameRoom> rooms;


    public RoomCoordinator() {
        this.rooms = new ArrayList<GameRoom>();
    }

    public void addRoom(GameRoom gameRoom){
        this.rooms.add(gameRoom);
    }

    public GameRoom getRoomByCode(int roomId) {
        for (GameRoom room : rooms) {
            if (room.getRoomCode() == roomId) {
                return room;
            }
        }
        return null; // Return null if no room with the given id is found
    }
}
