package ch.uzh.ifi.hase.soprafs23.game;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;

//set instance in socketController so that server does not need to be passed arround all the time
public class VoteController {
    private List<Vote> votes = new ArrayList<Vote>();

    //private boolean votingOpen = true;

    private TimerController timer;

    private SocketIOServer server;

    public VoteController(TimerController timer, SocketIOServer server){
        this.timer = timer;
        this.server = server;

        server.addEventListener("send_vote", Vote.class, onVoteReceived());

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

    public void setVote(long userId, String voteAnswer){
        if(timer.getTimer().isRunning()) {
            Vote vote = new Vote();
            vote.setTime(timer.getTimer().getRemainingTimeInSeconds());
            vote.setVote(voteAnswer);
            vote.setPlayerId(userId);
            votes.add(vote);
        }
    }

    private DataListener<Vote> onVoteReceived(){
        return (senderClient, data, ackSender) -> {
            System.out.println("vote received:");
    
            setVote(data.getPlayerId(),data.getVote());

        };
    }

    public List<Vote> getVotes(){
        return votes;
    }

    public void resetVotes(){
        this.votes = new ArrayList<Vote>();
    }
}
