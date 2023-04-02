package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GAMEROOM")
public class GameRoomRepo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private List<User> members;
    @Column(nullable = false)
    private User leader;
    @Column(nullable = false)
    private int numOfQuestions;
    @Column(nullable = false)
    private String questionTopic;
    @Column(nullable = false, unique = true)
    private int roomCode;
    @Column(nullable = false)
    private String gameMode;


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

