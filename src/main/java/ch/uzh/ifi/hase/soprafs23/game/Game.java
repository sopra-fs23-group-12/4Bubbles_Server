package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.service.SocketBasics;


import java.util.List;

public class Game {
    private GameRoom gameRoom;

    private List<Question> questions;

    private GameRanking ranking;

    private int roundCounter;

    private TimerController timerController;
    private String roomCode;

    private final SocketBasics socketBasics;

    private VoteController voteController;


    public Game(GameRoom gameRoom){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.questions = this.gameRoom.getQuestions();
        this.roundCounter = this.gameRoom.getQuestions().size();
        this.roomCode = this.gameRoom.getRoomCode();
        this.voteController = this.gameRoom.getVoteController();
        this.timerController = new TimerController();
        this.socketBasics =  new SocketBasics();


    }

    public void startGame(){
        
        //maybe turn this in to a timer before the first question is sent
        socketBasics.sendObject(this.gameRoom.getRoomCode(),EventNames.GAME_STARTED.eventName,  "");

        //have 5 second timer before the game starts, then send the question, then have 3 second timer
        timerController.setTimer(5);
        timerController.startTimer(roomCode);

        sendQuestion();

        timerController.setTimer(3);
        timerController.startTimer(roomCode);
        sendAnswers();

        /*
        can no longer be automated because the timer now runs in the frontend
        while(roundCounter > 0){
            System.out.println("Question" + roundCounter);
            playRound();
            this.voting.resetVotes();
            roundCounter--;
        }*/
    }

    public void setVoteGame(long userId, String message, int remainingTime){
        voteController.setVote(userId, message,remainingTime);
        List<Vote> votes = voteController.getVotes();
        System.out.print(votes);

    }

    //send the timer pings
    //receive the votes and broadcast them to the other players
    //send the correct answers
    public void playRound(){
        sendQuestion();

        timerController.setTimer(3);
        timerController.startTimer(roomCode);

        sendAnswers();
        timerController.setTimer(10);
        timerController.startTimer(roomCode);

        List<Vote> votes = voteController.getVotes();
        String currentRanking = ranking.updateRanking(questions.get(roundCounter-1), votes).values().toString();
        socketBasics.sendObject(roomCode,EventNames.RECEIVE_VOTING.eventName, currentRanking);

    }



    private void sendQuestion(){
        socketBasics.sendObject(roomCode,EventNames.GET_QUESTION.eventName,  gameRoom.getQuestions().get(roundCounter-1).getQuestion());

    }


    //list of answers is converted to a string to comply with constructor of Message Type
    //must be converted back to list in frontend
    private void sendAnswers(){
        socketBasics.sendObject(roomCode,EventNames.GET_ANSWERS.eventName,  gameRoom.getQuestions().get(roundCounter-1).getAnswers().toString());
        
        
    }

    

    public int getRemainingTime(){
        return this.timerController.getTimer().getRemainingTimeInSeconds();
    }

    public int getRoundCounter(){return this.roundCounter;}


    

}
