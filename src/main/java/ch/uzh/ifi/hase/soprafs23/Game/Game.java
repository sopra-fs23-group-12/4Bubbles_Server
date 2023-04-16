package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class Game {
    private GameRoom gameRoom;
    private List<Question> questions;

    private GameRanking ranking;

    private MockTriviaCaller triviaCaller;

    private int roundCounter;

    private MockVoting voting;

    private TimerController timer;

    public Game(GameRoom gameRoom){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.triviaCaller = new MockTriviaCaller();
        this.questions = triviaCaller.getTriviaQuestions();
        this.timer = new TimerController();
        this.voting = new MockVoting(timer);
        this.roundCounter = 1;
    }



    public void startGame(){
        //call triviaCaller with configs that are specified in gameRoom
        while(roundCounter >= 0){
            playRound();
            this.voting.resetVotes();
            roundCounter--;
        }
    }

    public void playRound(){
        System.out.println(questions.get(roundCounter).getQuestion() + ": " + questions.get(roundCounter).getAnswers() + " " + questions.get(roundCounter).getNumOfCorrectAnswer());
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
