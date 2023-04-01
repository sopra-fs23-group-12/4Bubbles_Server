package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import lombok.Data;

//this class is for the websocket test

//from https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
@Data
public class Message {
    private MessageType type;
    private String message;
    private String room;

    public Message() {
    }
    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getRoom(){
        return this.room;
    }

    public String getMessage(){
        return this.message;
    }

    public MessageType getType(){
        return this.type;
    }
}



