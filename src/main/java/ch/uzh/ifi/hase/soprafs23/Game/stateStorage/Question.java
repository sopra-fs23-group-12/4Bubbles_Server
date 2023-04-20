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
    private String correctAnswer;


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

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }




}
