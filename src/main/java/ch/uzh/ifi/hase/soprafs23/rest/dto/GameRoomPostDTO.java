package ch.uzh.ifi.hase.soprafs23.rest.dto;
import ch.uzh.ifi.hase.soprafs23.entity.User;

public class GameRoomPostDTO {
    
    
    private long leader;
    private int numOfQuestions;
    private String questionTopic;
    private String gameMode;

    
    public long getLeader() {
        return leader;
    }
    public void setLeader(long leader) {
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
    public String getGameMode() {
        return gameMode;
    }
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }


}