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

    //called upon startGame, does the things that only need to happen before the first question is started
    public void startPreGame(){

        socketBasics.sendObjectToRoom(this.gameRoom.getRoomCode(),EventNames.GAME_STARTED.eventName,  "");

        //have 5 second timer before the game starts, then send the question, then have 3 second timer
        timerController.setTimer(5);
        timerController.startTimer(roomCode);
    }

    //one iteration of a question
    public void startGame(){
        
        sendAnswers();//send all answers as well as correct answer
        sendQuestion();

        timerController.setTimer(3);
        timerController.startTimer(roomCode);
        

        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                timerController.setTimer(10);
                timerController.startTimer(roomCode);
                socketBasics.sendObjectToRoom(roomCode, "end_of_question", 0);
            }
        });
        timerThread.start();

        roundCounter--;
    }

    public void setVoteGame(long userId, String message, int remainingTime){
        voteController.setVote(userId, message,remainingTime);
        List<Vote> votes = voteController.getVotes();
        System.out.print(votes);

    }



    private void sendQuestion(){
        socketBasics.sendObjectToRoom(roomCode,EventNames.GET_QUESTION.eventName,  gameRoom.getQuestions().get(roundCounter-1).getQuestion());

    }


    //list of answers is converted to a string to comply with constructor of Message Type
    //must be converted back to list in frontend
    private void sendAnswers(){
        socketBasics.sendObjectToRoom(roomCode, EventNames.GET_RIGHT_ANSWER.eventName, gameRoom.getQuestions().get(roundCounter-1).getCorrectAnswer());
        socketBasics.sendObjectToRoom(roomCode,EventNames.GET_ANSWERS.eventName,  gameRoom.getQuestions().get(roundCounter-1).getAnswers());
        
        
    }

    

    public int getRemainingTime(){
        return this.timerController.getTimer().getRemainingTimeInSeconds();
    }

    public int getRoundCounter(){return this.roundCounter;}

    public GameRanking getRanking(){return this.ranking;}
    
    public String getRoomCode(){return this.roomCode;}

    public List<Question> getQuestions(){return this.questions;}

    public VoteController getVoteController() {
        return voteController;
    }
}
