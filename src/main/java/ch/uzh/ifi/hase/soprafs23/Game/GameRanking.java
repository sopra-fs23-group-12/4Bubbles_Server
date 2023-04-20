package ch.uzh.ifi.hase.soprafs23.game;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRanking {
    private Map<Long, Integer> rankingDict;

    public GameRanking(List<User> members) {
        rankingDict = new HashMap<>();
        for (User member : members) {
            rankingDict.put(member.getId(), 0);
        }
    }


    public Map<Long, Integer> updateRanking(Question question, List<Vote> votes) {
        for(Vote vote : votes){
            int oldPoints = rankingDict.get(vote.getPlayerId());
            int addPoints = 0;
            if (vote.getVote() == question.getCorrectAnswer()){
                addPoints = vote.getTime()*10;
            }
            rankingDict.put(vote.getPlayerId(), oldPoints + addPoints);
        }
        return rankingDict;
    }
}
