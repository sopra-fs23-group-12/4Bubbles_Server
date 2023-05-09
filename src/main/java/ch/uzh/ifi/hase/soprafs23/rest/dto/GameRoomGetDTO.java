package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.entity.User;

public class GameRoomGetDTO {

    private Map<Long, User> members;
    private User leader;
    private int numOfQuestions;
    private String questionTopic;
    private String roomCode;
    private String gameMode;


    public Map<Long, User> getMembers() {
        return members;
    }

    public void setMembers(Map<Long, User> members) {
        this.members = members;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public String getQuestionTopic() {
        return questionTopic;
    }

    public void setQuestionTopic(String questionTopic) {
        this.questionTopic = questionTopic;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }


}
