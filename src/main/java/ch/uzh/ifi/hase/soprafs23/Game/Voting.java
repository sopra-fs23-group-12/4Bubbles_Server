package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;

import java.util.List;

public interface Voting {

    public void setVote(String playerName, int voteNum);

    public List<Vote> getVotes();

    public void resetVotes();
}
