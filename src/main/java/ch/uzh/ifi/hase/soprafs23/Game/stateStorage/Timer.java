package ch.uzh.ifi.hase.soprafs23.Game.stateStorage;

public class Timer {
    private final int timeInSeconds;
    private int elapsedTimeInSeconds;

    public Timer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        this.elapsedTimeInSeconds = 0;
    }

    public void start() {
        try {
            for (int i = timeInSeconds; i > 0; i--) {
                Thread.sleep(1000);
                elapsedTimeInSeconds++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public int getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }

    public int getRemainingTimeInSeconds() {
        return timeInSeconds-elapsedTimeInSeconds;
    }
}