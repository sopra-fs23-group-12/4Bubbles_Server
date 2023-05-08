package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class UserPointsPutDTO {

    private long id;
    private int points;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    
}
