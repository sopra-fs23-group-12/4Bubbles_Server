package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.service.SocketService;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;


import java.util.List;

public class Game {
    private GameRoom gameRoom;

    private List<Question> questions;

    private GameRanking ranking;

    private int roundCounter;

    private VoteController voting;

    private TimerController timer;

    private SocketService socketService;

    private SocketIOClient clients;

    public Game(GameRoom gameRoom, SocketService socketService, SocketIOClient clients, SocketIOServer server){
        this.gameRoom = gameRoom;
        this.socketService = socketService;
        this.clients = clients;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.questions = this.gameRoom.getQuestions();
        this.timer = new TimerController();
        this.voting = new VoteController(timer, server);
        this.roundCounter = this.gameRoom.getQuestions().size();

    }



    public void startGame(){
        //call triviaCaller with configs that are specified in gameRoom
        while(roundCounter >= 0){
            playRound();
            this.voting.resetVotes();
            roundCounter--;
        }
    }

    //send the new question and answers (how? What object do you send?)
    //send the timer pings
    //receive the votes and broadcast them to the other players
    //send the correct answers
    public void playRound(){
        sendQuestion();
        
        sendAnswers();
        timer.startTimer();
        List<Vote> votes = voting.getVotes();
        socketService.sendMessage(this.gameRoom.getRoomCode(),"send_Ranking", clients,  ranking.updateRanking(questions.get(roundCounter-1), votes).values().toString());
        timer.resetTimer();
    }

    private void sendQuestion(){
        socketService.sendMessage(this.gameRoom.getRoomCode(),"send_Question", clients,  gameRoom.getQuestions().get(roundCounter-1).getQuestion());

        timer.startQuestionTimer();
        timer.resetQuestionTimer();
    }


    //list of answers is converted to a string to coply with constructor of Message Type
    //must be converted back to list in frontend
    private void sendAnswers(){
        socketService.sendMessage(this.gameRoom.getRoomCode(),"send_Answers", clients,  gameRoom.getQuestions().get(roundCounter-1).getAnswers().toString());
        
        
    }

    public int getRemainingTime(){
        return this.timer.getTimer().getRemainingTimeInSeconds();
    }



}
