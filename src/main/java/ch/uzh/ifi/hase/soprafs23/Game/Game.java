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

    private TriviaCaller triviaCaller;

    private int roundCounter;

    private Voting voting;

    private Timer timer;

    public Game(GameRoom gameRoom){
        this.gameRoom = gameRoom;
        this.ranking  = new GameRanking(gameRoom.getMembers());
        this.triviaCaller = new MockTriviaCaller();
        this.questions = triviaCaller.getTriviaQuestions(2, "Geography");
        this.timer = new Timer(10);
        this.voting = new MockVoting(timer);
        this.roundCounter = 1;
    }

    public void startGame(){
        while(roundCounter >= 0){
            playRound();
            this.voting.resetVotes();
            roundCounter--;
        }
    }

    public void playRound(){
        System.out.println(questions.get(roundCounter).getQuestion() + ": " + questions.get(roundCounter).getAnswers() + " " + questions.get(roundCounter).getNumOfCorrectAnswer());

        //in a new thread set votes to emulate vote incoming from frontend, these two lines can be removed when the websockets should be used
        MockVoting voting = (MockVoting) this.voting;
        voting.initMockVotes();

        timer.start();
        List<Vote> votes = voting.getVotes();
        ranking.updateRanking(questions.get(roundCounter), votes);
        timer.reset();
    }
}
