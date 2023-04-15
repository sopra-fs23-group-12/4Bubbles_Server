package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;

import java.util.ArrayList;
import java.util.List;

public class MockVoting implements Voting {
        private List<Vote> votes = new ArrayList<Vote>();

        private Timer timer;

        public MockVoting(Timer timer){
            this.timer = timer;
            initMockVotes();
        }

        public void initMockVotes(){
            //this simulates calls to set votes from frontend
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    setVote("playerName1",2);
                    Thread.sleep(2000);
                    setVote("playerName2", 4);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }

        public void setVote(String playerName, int voteNum){
            if(timer.isRunning()) {
                Vote vote = new Vote();
                vote.setTime(timer.getRemainingTimeInSeconds());
                vote.setVoteNum(voteNum);
                vote.setPlayerName(playerName);
                votes.add(vote);
            }
        }

        public List<Vote> getVotes(){
            return votes;
        }

        public void resetVotes(){
            this.votes = new ArrayList<Vote>();
        }
}
