package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Question;

import java.util.List;

public class MockTriviaCaller {
    //create 1 Mock Trivia Question
    Question triviaQuestion1 = new Question();
    Question triviaQuestion2 = new Question();

    List<Question> questions = List.of(triviaQuestion1, triviaQuestion2);

    public List<Question> getTriviaQuestions() {
        return questions;
    }
}
