package ch.uzh.ifi.hase.soprafs23.Game.stateStorage;

import org.springframework.stereotype.Component;

public class TimerController {
    private Timer timer = new Timer(10);

    public Timer getTimer(){
        return timer;
    }

    public void startTimer(){
        timer.start();
    }

    public void resetTimer(){
        timer = new Timer(10);
    }
}
