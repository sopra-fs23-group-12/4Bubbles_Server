package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class QuestionGetDTO {

    private String question;
    private String correctAnswer;
    private List<Object> incorrectAnswers;
    
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public List<Object> getIncorrectAnswers() {
        return incorrectAnswers;
    }
    public void setIncorrectAnswers(List<Object> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

}
