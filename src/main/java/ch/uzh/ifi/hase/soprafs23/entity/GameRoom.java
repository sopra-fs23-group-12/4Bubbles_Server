package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.List;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs23.GameModes.Mode;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GAMEROOM")
public class GameRoom {


    @Id
    @GeneratedValue
    private Long id;

/*    private UserService userService;

    private List<User> members;
    //private User leader;
    private int numOfQuestions;
    private String questionTopic;
    private int roomCode;
    private List<Mode> availableModes;*/
    private int gameMode;
/*    private Long id;

    public GameRoom() {
        this.roomCode = generateRoomCode();
    }*/

    //public List<User> getMembers() {
    //    return members;
    //}

/*    public void addPlayer(long userId, String bearerToken) {
        User player = userService.getUser(userId, bearerToken);
        members.add(player);
    }*/

    //public User getLeader() {
    //    return leader;
    //}

    //public void setLeader(User leader) {
    //    this.leader = leader;
    //}

/*    public int getNumOfQuestions() {
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
    }*/


    //public List<Mode> getAvailableModes() {
    //    return availableModes;
    //}
    /* public void setAvailableModes(List<Mode> availableModes) {
        this.availableModes = availableModes;
    } */

    //public Mode getGameMode() {
    //    return gameMode;
    //}

/*    public void setGameMode(Mode gameMode) {
        this.gameMode = gameMode;
    }*/

/*    public int generateRoomCode() {
        Random code = new Random();
        return code.nextInt(100000, 1000000);
    }


    public void setId(Long id) {
        this.id = id;
    }*/

/*    @Id
    public Long getId() {
        return id;
    }*/
}

