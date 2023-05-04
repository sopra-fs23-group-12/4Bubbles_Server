package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.game.Game;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RoomCoordinator {
    private final ArrayList<GameRoom> rooms;
    private static RoomCoordinator INSTANCE;

    public static RoomCoordinator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoomCoordinator();
        }

        return INSTANCE;
    }

    private RoomCoordinator() {
        this.rooms = new ArrayList<GameRoom>();
    }

    public void addRoom(GameRoom gameRoom){
        this.rooms.add(gameRoom);
    }

    public GameRoom getRoomByCode(String roomId) throws NotFoundException {
        for (GameRoom room : rooms) {
            if (room.getRoomCode().equals(roomId)) {
                return room;
            }
        }
        throw new NotFoundException("Room with given room code could not be found");
    }

    public void deleteRoom(String roomCode){
        try{ GameRoom gameRoom = getRoomByCode(roomCode);
            this.rooms.remove(gameRoom);
        } catch (Exception e){
            System.out.println("room could not be deleted because it wasn't found");
        }

    }
}
