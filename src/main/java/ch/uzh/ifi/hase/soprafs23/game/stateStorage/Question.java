package ch.uzh.ifi.hase.soprafs23.game.stateStorage;

import java.util.List;

public class Question {

    private String questionString;
    private List<String> answers;
    private String correctAnswer;


    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String question) {
        this.questionString = question;
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
