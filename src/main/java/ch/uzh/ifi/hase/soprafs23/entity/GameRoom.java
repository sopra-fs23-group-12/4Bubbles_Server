package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.List;

public class GameRoom {

    private List<User> members;
    private User leader;
    private int numOfQuestions;
    private String questionTopic;
    private int roomCode;
    private String gameMode;
    private long leaderUserId;

    public long getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(long leaderUserId) {
        this.leaderUserId = leaderUserId;
    }

    public List<User> getMembers() {
        return members;
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

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}

