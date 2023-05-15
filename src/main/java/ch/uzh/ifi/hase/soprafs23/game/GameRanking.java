package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRanking {
    private Map<Long, Integer> rankingDict;


    public GameRanking(Map<Long, User> map) {
        rankingDict = new HashMap<>();
        for (long memberId : map.keySet()) {
            rankingDict.put(memberId, 0);
        }
    }

    public Map<Long, Integer> updateRanking(Question question, Map<Long, Vote> votes) {
        for(Vote vote : votes.values()){
            int oldPoints = rankingDict.get(vote.getPlayerId());
            int addPoints = 0;
            if (vote.getVote().equals(question.getCorrectAnswer())){
                addPoints = vote.getRemainingTime()*10;
            }
            rankingDict.put(vote.getPlayerId(), oldPoints + addPoints);
        }
        return rankingDict;
    }
}
