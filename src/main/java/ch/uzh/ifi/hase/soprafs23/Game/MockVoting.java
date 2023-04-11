package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;

import java.util.ArrayList;
import java.util.List;

public class MockVoting {
        private List<Vote> votes;

        private boolean votingOpen;

        private Timer timer;

        public MockVoting(Timer timer){
            this.timer = timer;
            Vote vote1 = new Vote();
            Vote vote2 = new Vote();
            votes = new ArrayList<Vote>();
            votes.add(vote1);
            votes.add(vote2);
        }

        public void setVote(String playerName, int voteNum){
            Vote vote = new Vote();
            vote.setTime(timer.getRemainingTimeInSeconds());
            vote.setVoteNum(voteNum);
            vote.setPlayerName(playerName);
            votes.add(vote);
        }

        public List<Vote> getVotes(){
            return votes;
        }





}
