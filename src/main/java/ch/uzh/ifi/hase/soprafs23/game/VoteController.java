package ch.uzh.ifi.hase.soprafs23.game;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs23.entity.Vote;


//set instance in socketController so that server does not need to be passed around all the time
public class VoteController {
    
    private Map<Long,Vote> votes = new HashMap<Long, Vote>();
  

    public void setVote(long userId, String voteAnswer, int remainingTime){
            Vote vote = new Vote();
            vote.setRemainingTime(remainingTime);
            vote.setVote(voteAnswer);
            vote.setPlayerId(userId);
            System.out.println(vote.getVote());
            System.out.println(vote.getPlayerId());
            System.out.println(vote.getRemainingTime());
            votes.put(userId, vote);
    }

    public Map<Long, Vote> getVotes(){
        return votes;
    }


    public void resetVotes(){
        this.votes = new HashMap<Long, Vote>();
    }
}
