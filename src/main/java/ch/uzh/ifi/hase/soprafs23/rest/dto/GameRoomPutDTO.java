package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class GameRoomPutDTO {

    private String roomCode;
    private long userId;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
