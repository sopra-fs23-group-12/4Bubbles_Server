package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GameRoomJoinDTO {

    private int roomCode;
    private int userId;

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
