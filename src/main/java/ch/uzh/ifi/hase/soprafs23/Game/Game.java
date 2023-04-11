package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;

import java.util.List;

public class Game {
    private GameRoom gameRoom;
    private List<Question> questions;

    private GameRanking ranking;

    private MockTriviaCaller triviaCaller;

    private int roundCounter;
    private MockVoting voting;

    public Game(GameRoom gameRoom){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.triviaCaller = new MockTriviaCaller();
        this.questions = triviaCaller.getTriviaQuestions();
        this.roundCounter = 2;

    }

    public void startGame(){
        //call triviaCaller with configs that are specified in gameRoom
        while(roundCounter > 0){
            playRound();
        }
    }



    public void playRound(){
        Timer timer = new Timer(10);
        this.voting = new MockVoting(timer);
        timer.start();
        List<Vote> votes = voting.getVotes();
        ranking.updateRanking(questions.get(roundCounter), votes);
    }


}
