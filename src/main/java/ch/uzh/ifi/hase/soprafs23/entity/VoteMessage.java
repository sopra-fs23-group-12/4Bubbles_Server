package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.MessageType;
import lombok.Data;

public class VoteMessage {

        private MessageType type;
        private String message;
        private int remainingTime;
        private String roomCode;

        private String userId;

        private String bearerToken;

        public VoteMessage() {
        }
        public VoteMessage(MessageType type, String message, int remainingTime) {
            this.type = type;
            this.message = message;
            this.remainingTime = remainingTime;
        }

        public String getRoomCode(){
            return this.roomCode;
        }

        public String getMessage(){
            return this.message;
        }

        public MessageType getType(){
            return this.type;
        }

        public String getUserId(){return this.userId;}

        public String getBearerToken() {return this.bearerToken;}

        public int getRemainingTime(){return this.remainingTime;}


}
