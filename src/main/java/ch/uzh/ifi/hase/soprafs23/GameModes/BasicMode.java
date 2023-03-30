package ch.uzh.ifi.hase.soprafs23.GameModes;

import java.util.HashMap;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.User;

public abstract class BasicMode implements Mode {

    /* just an initial implementation
     * will be changed later
     */
    protected int numOfQuestions;
    protected String questionTopic;
    protected String gameMode;
    protected boolean fooling;
    protected String question;
    protected List<String> answerChoices;
    protected int correctAnswer;
    protected int numOfCurrentQuestions;
    protected HashMap<User, Integer> votes;
    protected HashMap<User, Integer> scores;
    protected int bubbleSize;


    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public String getQuestionTopic() {
        return questionTopic;
    }

    public String getGameMode() {
        return gameMode;
    }

    public boolean isFooling() {
        return fooling;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswerChoices() {
        return answerChoices;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public int getNumOfCurrentQuestions() {
        return numOfCurrentQuestions;
    }

    public HashMap<User, Integer> getVotes() {
        return votes;
    }

    public HashMap<User, Integer> getScores() {
        return scores;
    }

    public int getBubbleSize() {
        return bubbleSize;
    }


}
