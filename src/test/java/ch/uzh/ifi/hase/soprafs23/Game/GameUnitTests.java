package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Vote;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameUnitTests {

    private User testUser1;
    private User testUser2;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("playerName1");
        testUser1.setPassword("password1");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("playerName2");
        testUser2.setPassword("password2");
    }

    @Test
    public void testStartPreGame() throws Exception
    {
        /*GameRoom gameRoom = new GameRoom();
        gameRoom.setLeaderUserId(1);
        gameRoom.setRoomCode("123456");
        gameRoom.setMembers(Map.of(1L,testUser1));
        gameRoom.setQuestions(List.of(new Question(), new Question(), new Question()));

        Game game = new Game(gameRoom);

        game.startPreGame();
        game.startGame();
        game.getRanking();
        Assertions.assertEquals(game.getRoundCounter(), gameRoom.getQuestions().size());*/
    }

    @Test
    public void testSetVoteGame() throws Exception
    {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setLeaderUserId(1);
        gameRoom.setRoomCode("123456");
        gameRoom.setMembers(Map.of(1L,testUser1));
        gameRoom.setQuestions(List.of(new Question(), new Question(), new Question()));

        Game game = new Game(gameRoom);

        game.setVoteGame(1, "answer1", 5);
        game.getVoteController().getVotes();

        Vote checkVote = new Vote();
        checkVote.setPlayerId(1);
        checkVote.setVote("answer1");
        checkVote.setRemainingTime(5);

        Assertions.assertEquals(checkVote.getVote(), game.getVoteController().getVotes().get(1L).getVote());
        game.getVoteController().resetVotes();
    }

    /*@Test
    public void testCommunication() throws Exception
    {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setLeaderUserId(1);
        gameRoom.setRoomCode("123456");
        gameRoom.setMembers(List.of(testUser1));
        gameRoom.setQuestions(List.of(new Question(), new Question(), new Question()));

        Game game = new Game(gameRoom);
        game.sendQuestion();
    }*/
}
