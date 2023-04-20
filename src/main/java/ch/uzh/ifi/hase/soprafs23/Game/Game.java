package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class Game {
    private GameRoom gameRoom;

    private List<Question> questions;

    private GameRanking ranking;

    private int roundCounter;

    private MockVoting voting;

    private TimerController timer;

    public Game(GameRoom gameRoom){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.questions = this.gameRoom.getQuestions();
        this.timer = new TimerController();
        this.voting = new MockVoting(timer);
        this.roundCounter = this.questions.size();
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
        System.out.println(questions.get(roundCounter).getQuestion() + ": " + questions.get(roundCounter).getAnswers() + " " + questions.get(roundCounter).getCorrectAnswer());
        //in a new thread allow to set votes
        voting.initMockVotes();
        timer.startTimer();
        List<Vote> votes = voting.getVotes();
        ranking.updateRanking(questions.get(roundCounter), votes);
    }

    public int getRemainingTime(){
        return this.timer.getTimer().getRemainingTimeInSeconds();
    }

    public MockVoting getVotingSystem(){
        return this.voting;
    }



}
