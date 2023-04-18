package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.Game.stateStorage.Question;

import java.util.List;

public interface TriviaCaller {
    public List<Question> getTriviaQuestions(int numOfQuestions, String questionTopic);
}
