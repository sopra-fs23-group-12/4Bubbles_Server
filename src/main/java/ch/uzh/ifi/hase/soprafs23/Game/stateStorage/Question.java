package ch.uzh.ifi.hase.soprafs23.Game.stateStorage;

import java.util.List;

public class Question {

    /*public Question(String question, List<String> answers, int numOfCorrectAnswer) {
        this.question = question;
        this.answers = answers;
        this.numOfCorrectAnswer = numOfCorrectAnswer;
    }*/

    private String question;
    private List<String> answers;
    private int numOfCorrectAnswer;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getNumOfCorrectAnswer() {
        return numOfCorrectAnswer;
    }

    public void setNumOfCorrectAnswer(int numOfCorrectAnswer) {
        this.numOfCorrectAnswer = numOfCorrectAnswer;
    }




}
