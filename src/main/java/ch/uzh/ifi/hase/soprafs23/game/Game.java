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

    private TimerController timer;

    private final SocketBasics socketBasics = new SocketBasics();


    public Game(GameRoom gameRoom, SocketIOServer server){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.questions = this.gameRoom.getQuestions();
        this.timer = new TimerController();
        this.voting = new VoteController(timer, server);
        this.roundCounter = this.gameRoom.getQuestions().size();

    }



    public void startGame(){
        
        //maybe trun this in to a timer before the first question is sent
        socketBasics.sendObject(this.gameRoom.getRoomCode(),EventNames.GAME_STARTED.eventName,  "");
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
        
        sendAnswers();
        timer.startTimer(this.gameRoom.getRoomCode());
        List<Vote> votes = voting.getVotes();
        socketBasics.sendObject(this.gameRoom.getRoomCode(),EventNames.GET_RANKING.eventName, ranking.updateRanking(questions.get(roundCounter-1), votes).values().toString());
        timer.resetTimer();
    }

    private void sendQuestion(){
        socketBasics.sendObject(this.gameRoom.getRoomCode(),EventNames.GET_QUESTION.eventName,  gameRoom.getQuestions().get(roundCounter-1).getQuestion());

        timer.startQuestionTimer(this.gameRoom.getRoomCode());
        timer.resetQuestionTimer();
    }


    //list of answers is converted to a string to coply with constructor of Message Type
    //must be converted back to list in frontend
    private void sendAnswers(){
        socketBasics.sendObject(this.gameRoom.getRoomCode(),EventNames.GET_ANSWERS.eventName,  gameRoom.getQuestions().get(roundCounter-1).getAnswers().toString());
        
        
    }

    public int getRemainingTime(){
        return this.timer.getTimer().getRemainingTimeInSeconds();
    }



}
