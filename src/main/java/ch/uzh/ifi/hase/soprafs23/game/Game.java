package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.service.SocketBasics;

import com.corundumstudio.socketio.SocketIOServer;


import java.util.List;

public class Game {
    private GameRoom gameRoom;

    private List<Question> questions;

    private GameRanking ranking;

    private int roundCounter;

    private VoteController voting;

    private TimerController timerController;
    private String roomCode;

    private final SocketBasics socketBasics = new SocketBasics();


    public Game(GameRoom gameRoom){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.questions = this.gameRoom.getQuestions();
        this.roundCounter = this.gameRoom.getQuestions().size();
        this.roomCode = this.gameRoom.getRoomCode();
        this.timerController = new TimerController();

    }

    //needs to be called when a new game is created by the socketController upon starting
    //the game such that game and SocketController have access to the same voteController instance
     public void setVoteController(VoteController voteController){
            this.voting = voteController;
     }

    public void startGame(){
        
        //maybe turn this in to a timer before the first question is sent
        socketBasics.sendObject(roomCode,EventNames.GAME_STARTED.eventName,  "");
        while(roundCounter > 0){
            System.out.println("Question" + roundCounter);
            playRound();
            this.voting.resetVotes();
            roundCounter--;
        }
    }

    //send the timer pings
    //receive the votes and broadcast them to the other players
    //send the correct answers
    public void playRound(){
        sendQuestion();

        timerController.startQuestionTimer(roomCode);
        timerController.resetQuestionTimer();
        
        sendAnswers();
        timerController.startTimer(roomCode);
        //you need access to the votes that would be set in socketservice or socketcontroller

        //why access votes here? Just to send them all to the client?
        // is this necessary or can everything regarding the votes just be handled by the votecontroller?

        //since the line after the next is the only line where ranking is used in this class, we could simply instantiate this in the votecontroller instead and do the work there
        List<Vote> votes = voting.getVotes();
        String currentRanking = ranking.updateRanking(questions.get(roundCounter-1), votes).values().toString();
        socketBasics.sendObject(roomCode,EventNames.RECEIVE_VOTING.eventName, currentRanking);
        timerController.resetTimer();
    }

    private void sendQuestion(){
        socketBasics.sendObject(roomCode,EventNames.GET_QUESTION.eventName,  gameRoom.getQuestions().get(roundCounter-1).getQuestion());

    }


    //list of answers is converted to a string to comply with constructor of Message Type
    //must be converted back to list in frontend
    private void sendAnswers(){
        socketBasics.sendObject(roomCode,EventNames.GET_ANSWERS.eventName,  gameRoom.getQuestions().get(roundCounter-1).getAnswers().toString());
        
        
    }

    /*

    public int getRemainingTime(){
        return this.timer.getTimer().getRemainingTimeInSeconds();
    }



     */

}
