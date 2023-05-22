package ch.uzh.ifi.hase.soprafs23.Entity;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.VoteMessage;
import org.junit.jupiter.api.Test;

public class MessagesUnitTests {

    @Test
    public void testMessageGettersAndSetters(){

        Message message = new Message(MessageType.SERVER, "Hello Server");

        message.getRoomCode();

        message.getMessageString();

        message.getType();

        message.getUserId();

        message.getBearerToken();

        message.getRound();
    }

    @Test
    public void testVoteMessageGettersAndSetters(){
        VoteMessage message = new VoteMessage(MessageType.SERVER, "Hello Server", 10);

        message.getRoomCode();

        message.getMessage();

        message.getType();

        message.getUserId();

        message.getBearerToken();

        message.getRemainingTime();
    }
}
