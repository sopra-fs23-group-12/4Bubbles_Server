package ch.uzh.ifi.hase.soprafs23.entity;

import javassist.NotFoundException;
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

    public GameRoom getRoomByCode(int roomId) throws NotFoundException {
        for (GameRoom room : rooms) {
            if (room.getRoomCode() == roomId) {
                return room;
            }
        }
        throw new NotFoundException("Room with given room code could not be found");
    }
}
