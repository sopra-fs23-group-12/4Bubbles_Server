package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Question;

import java.util.List;

public class MockTriviaCaller implements TriviaCaller{
    //create 1 Mock Trivia Question

    public List<Question> getTriviaQuestions(int numOfQuestions, String questionTopic) {
        Question triviaQuestion1 = new Question();
        triviaQuestion1.setQuestion("What is the largest Country in Africa by inhabitants?");
        triviaQuestion1.setAnswers(List.of("South Africa", "Congo", "Kenia", "Nigeria"));
        triviaQuestion1.setNumOfCorrectAnswer(4);
        Question triviaQuestion2 = new Question();
        triviaQuestion2.setQuestion("What is the largest Country in Europe by size?");
        triviaQuestion2.setAnswers(List.of("Germany", "Ukraine", "Spain", "Finland"));
        triviaQuestion2.setNumOfCorrectAnswer(2);
        List<Question> questions = List.of(triviaQuestion1, triviaQuestion2);
        return questions;
    }
}
