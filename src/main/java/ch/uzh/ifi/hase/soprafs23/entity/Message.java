package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import lombok.Data;

//this class is for the websocket test

//from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
@Data
public class Message {
    private MessageType type;
    private String messageString;
    private String roomCode;

    private String userId;

    private String bearerToken;
    private int round;

    public Message() {
    }
    public Message(MessageType type, String message) {
        this.type = type;
        this.messageString = message;
    }

    public String getRoomCode(){
        return this.roomCode;
    }

    public String getMessageString(){
        return this.messageString;
    }

    public MessageType getType(){
        return this.type;
    }

    public String getUserId(){return this.userId;}

    public String getBearerToken() {return this.bearerToken;}

    public int getRound(){return this.round;}
}



