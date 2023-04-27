package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.GameRanking;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.SocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameRankingTest {
    private GameRoom gameRoom;
    private GameRanking ranking;

    private List<Question> questions;

    private List<Vote> votes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        //setup GameRoom
        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setPassword("password1");
        testUser1.setUsername("playerName1");

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("password2");
        testUser2.setUsername("playerName2");

        User testUser3 = new User();
        testUser3.setId(3L);
        testUser3.setPassword("password3");
        testUser3.setUsername("testUsername2");

        List<User> users = List.of(testUser1, testUser2, testUser3);

        gameRoom = new GameRoom();
        gameRoom.setMembers(users);
        gameRoom.setLeader(testUser1);

        Question triviaQuestion1 = new Question();
        triviaQuestion1.setQuestion("What is the largest Country in Africa by inhabitants?");
        triviaQuestion1.setAnswers(List.of("South Africa", "Congo", "Kenia", "Nigeria"));
        triviaQuestion1.setCorrectAnswer("Nigeria");
        Question triviaQuestion2 = new Question();
        triviaQuestion2.setQuestion("What is the largest Country in Europe by size?");
        triviaQuestion2.setAnswers(List.of("Germany", "Ukraine", "Spain", "Finland"));
        triviaQuestion2.setCorrectAnswer("Ukraine");
        this.questions = List.of(triviaQuestion1, triviaQuestion2);

        this.votes = new ArrayList<Vote>();
        Vote vote1 = new Vote();
        vote1.setRemainingTime(8);
        vote1.setVote(" Nigeria");
        vote1.setPlayerId(1L);
        votes.add(vote1);

        Vote vote2 = new Vote();
        vote2.setRemainingTime(6);
        vote2.setVote("Kenia");
        vote2.setPlayerId(2L);
        votes.add(vote2);
    }

    @Test
    public void testCorrectVotingGivesPoints() {
        this.ranking  = new GameRanking(this.gameRoom.getMembers());

        Map<Long,Integer> newRanking = ranking.updateRanking(this.questions.get(0), this.votes);

        assertEquals(80,newRanking.get(1L));

    }


    @Test
    public void testFalseVotingGivesNoPoints() {
        this.ranking  = new GameRanking(this.gameRoom.getMembers());

        Map<Long,Integer> newRanking = ranking.updateRanking(this.questions.get(0), this.votes);

        assertEquals(0,newRanking.get(2L));

    }


    @Test
    public void testNoVotingGivesNoPoints() {
        this.ranking  = new GameRanking(this.gameRoom.getMembers());

        Map<Long,Integer> newRanking = ranking.updateRanking(this.questions.get(0), this.votes);

        assertEquals(0,newRanking.get(3L));

    }
}
