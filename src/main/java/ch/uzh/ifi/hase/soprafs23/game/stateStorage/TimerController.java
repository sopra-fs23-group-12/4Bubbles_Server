package ch.uzh.ifi.hase.soprafs23.game.stateStorage;

public class TimerController {
    private Timer timer;

    public void startTimer(String roomCode){
        timer.start(roomCode);
    }

    public void setTimer(int time){
        timer = new Timer(time);
    }
}
