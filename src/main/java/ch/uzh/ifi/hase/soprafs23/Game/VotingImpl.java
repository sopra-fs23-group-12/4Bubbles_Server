package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;

import java.util.ArrayList;
import java.util.List;

public class VotingImpl {
    private List<Vote> votes = new ArrayList<Vote>();

    private Timer timer;

    public VotingImpl(Timer timer){
        this.timer = timer;
    }

    //TODO: this should be called by websocket to set votes
    public void setVote(String playerName, int voteNum){
        if(timer.isRunning()) {
            Vote vote = new Vote();
            vote.setTime(timer.getRemainingTimeInSeconds());
            vote.setVoteNum(voteNum);
            vote.setPlayerName(playerName);
            votes.add(vote);
        }
    }

    //TODO: this should be called by websocket to get votes
    public List<Vote> getVotes(){
        return votes;
    }

    public void resetVotes(){
        this.votes = new ArrayList<Vote>();
    }
}
