package ch.uzh.ifi.hase.soprafs23.entity;


public class Vote {

    private long playerId;
    private String voteString;
    private int remainingTime;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getVoteString() {
        return voteString;
    }

    public void setVoteString(String vote) {
        this.voteString = vote;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
