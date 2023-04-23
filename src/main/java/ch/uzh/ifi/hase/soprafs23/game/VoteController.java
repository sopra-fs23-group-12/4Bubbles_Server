package ch.uzh.ifi.hase.soprafs23.game;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;

//set instance in socketController so that server does not need to be passed around all the time
public class VoteController {
    private List<Vote> votes = new ArrayList<Vote>();

    //private boolean votingOpen = true;

    private TimerController timer;

    private SocketIOServer server;

    public VoteController(){
        this.server = server;

        //this.server.addEventListener(EventNames.SEND_VOTE.eventName, Vote.class, onVoteReceived());
        

    }

    /* public void initVotes(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                
                setVote(vote.getPlayerId(),vote.getVote());
                
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    } */

    //later transfer to post request

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

    /*
    private DataListener<Vote> onVoteReceived(){
        return (senderClient, data, ackSender) -> {
            System.out.println("data listener in votecontroller");
            /*
        setVote(data.getPlayerId(),data.getVote());
        System.out.println("vote received:");
        */
/*
        };



    }*/

    public List<Vote> getVotes(){
        return votes;
    }

    public void resetVotes(){
        this.votes = new ArrayList<Vote>();
    }
}
