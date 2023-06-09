package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.VoteController;
import com.corundumstudio.socketio.SocketIONamespace;

import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

public class GameRoom {

    private Map<Long, User> members = new HashMap<>();
    private List<Question> questions;
    private User leader;
    private int numOfQuestions;
    private String questionTopic;
    private int questionTopicId;
    private String roomCode;
    private String gameMode;
    private String difficulty;
    private long leaderUserId;
    private boolean isGameStarted = false;

    

    private final VoteController voteController = new VoteController();

    private Game currentGame;
    private SocketIONamespace namespace = null;

    public long getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(long leaderUserId) {
        this.leaderUserId = leaderUserId;
    }

    public Map<Long,User> getMembers() {
        return members;
    }

    public void setMembers(Map<Long, User> members) {
        this.members = members;
    }

    public void setNamespace(SocketIONamespace namespace){
        this.namespace = namespace;
    }

    public SocketIONamespace getNamespace(){
        return namespace;
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

    public int getQuestionTopicId() {
        return questionTopicId;
    }

    public void setQuestionTopicId(int questionTopicId) {
        this.questionTopicId = questionTopicId;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public VoteController getVoteController() {
        return voteController;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public Game getCurrentGame(){
        return this.currentGame;
    }

    public void setCurrentGame(Game currentGame){
        this.currentGame = currentGame;
    }

}

