package ch.uzh.ifi.hase.soprafs23.game.stateStorage;

public class TimerController {
    private Timer timer = new Timer(10);
    private Timer questionTimer = new Timer(3);
    

    public Timer getQuestionTimer() {
        return questionTimer;
    }

    public Timer getTimer(){
        return timer;
    }

    public void startTimer(String roomCode){
        timer.start(roomCode);
    }

    public void startQuestionTimer(String roomCode){
        questionTimer.start(roomCode);
    }

    public void resetQuestionTimer(){
        questionTimer = new Timer(3);
    }

    public void resetTimer(){
        timer = new Timer(10);
    }
}
