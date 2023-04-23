package ch.uzh.ifi.hase.soprafs23.game;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;

public class MockVoting {
        private List<Vote> votes = new ArrayList<Vote>();

        private TimerController timer;

        public MockVoting(TimerController timer){
            this.timer = timer;
        }



        public void initMockVotes(){
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    setVote(1,"Nigeria");
                    Thread.sleep(2000);
                    setVote(2, "Ukraine");
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }

        //later transfer to post request

        public void setVote(long playerId, String voteAnswer){
            if(timer.getTimer().isRunning()) {
                Vote vote = new Vote();
                vote.setRemainingTime(timer.getTimer().getRemainingTimeInSeconds());
                vote.setVote(voteAnswer);
                vote.setPlayerId(playerId);
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
