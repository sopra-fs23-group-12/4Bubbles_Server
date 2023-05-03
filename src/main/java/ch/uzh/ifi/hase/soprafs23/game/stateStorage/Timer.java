package ch.uzh.ifi.hase.soprafs23.game.stateStorage;


import ch.uzh.ifi.hase.soprafs23.service.SocketBasics;

public class Timer {
    private final int timeInSeconds;
    private int elapsedTimeInSeconds;
    SocketBasics socketBasics = new SocketBasics();

    private boolean isRunning;


    public Timer(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        this.elapsedTimeInSeconds = 0;
    }

    public void start(String roomCode) {
        
        try {
            for (int i = timeInSeconds; i > 0; i--) {
                isRunning = true;
                Thread.sleep(1000);
                elapsedTimeInSeconds++;
                socketBasics.sendObjectToRoom(roomCode, "timer_count", i);
                System.out.println("Remaining time: " + (timeInSeconds-elapsedTimeInSeconds) + " seconds");
                 
                }
        }catch (InterruptedException e) {
            
        }

        isRunning = false;
        
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public int getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public int getRemainingTimeInSeconds() {
        return timeInSeconds-elapsedTimeInSeconds;
    }

    /* public static void main(String[] args) {
        
        Thread thread = new Thread(new Runnable() {
            Timer timer = new Timer(5);
            @Override
            public void run() {
                //Timer timer = new Timer(5);
                timer.setIsRunning(true);
                timer.start("test");
            }
        });
        thread.start();
        while (thread.isAlive()) {
            System.out.println("Timer is running");
        }

    } */
}