package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Timer;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class MockVoting {
        private List<Vote> votes = new ArrayList<Vote>();

        private boolean votingOpen = true;

        private TimerController timer;

        public MockVoting(TimerController timer){
            this.timer = timer;
        }



        public void initMockVotes(){
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    setVote("playerName1","Nigeria");
                    Thread.sleep(2000);
                    setVote("playerName2", "Ukraine");
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }

        //later transfer to post request

        public void setVote(String playerName, String voteAnswer){
            if(timer.getTimer().isRunning()) {
                Vote vote = new Vote();
                vote.setTime(timer.getTimer().getRemainingTimeInSeconds());
                vote.setVote(voteAnswer);
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
