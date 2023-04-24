package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import java.io.IOException;
import java.util.List;

public interface TriviaCaller {
    public List<Question> getQuestionsFromApi(String url) throws IOException ;
}
