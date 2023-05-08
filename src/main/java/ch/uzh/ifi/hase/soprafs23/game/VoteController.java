package ch.uzh.ifi.hase.soprafs23.game;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.Vote;


//set instance in socketController so that server does not need to be passed around all the time
public class VoteController {
    private List<Vote> votes = new ArrayList<Vote>();

    //private boolean votingOpen = true;

    public VoteController(){

    }

    

    public void setVote(long userId, String voteAnswer, int remainingTime){
            Vote vote = new Vote();
            vote.setRemainingTime(remainingTime);
            vote.setVote(voteAnswer);
            vote.setPlayerId(userId);
            System.out.println(vote.getVote());
            System.out.println(vote.getPlayerId());
            System.out.println(vote.getRemainingTime());
            votes.add(vote);
    }

    public List<Vote> getVotes(){
        return votes;
    }


    public void resetVotes(){
        this.votes = new ArrayList<Vote>();
    }
}
