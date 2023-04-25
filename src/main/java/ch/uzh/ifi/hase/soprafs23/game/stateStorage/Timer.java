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
                System.out.println("Remaining time: " + (timeInSeconds-elapsedTimeInSeconds) + " seconds");
                try {
                    System.out.println("Remaining time: " + (timeInSeconds - elapsedTimeInSeconds) + " seconds");
                   
                    socketBasics.sendObjectToRoom(roomCode, "timer_count", i);
                    
                }
                catch (Exception e){
                    System.out.print("room you want to set the timer in does not exist");
                    
                }
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
            Thread.currentThread().interrupt();
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

    public int getRemainingTimeInSeconds() {
        return timeInSeconds-elapsedTimeInSeconds;
    }
}