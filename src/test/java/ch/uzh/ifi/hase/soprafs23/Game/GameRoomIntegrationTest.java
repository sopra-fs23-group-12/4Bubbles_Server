package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.VoteController;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


//test, if the instances of attributes of GameRoom are imported correctly into the Game instance instantiated with it
public class GameRoomIntegrationTest {

    private GameRoom gameRoom;
    private Game game;

    private List<Question> questions;
    private Map<Long, User> members;

    @BeforeEach
    public void setup() {


        Question triviaQuestion1 = new Question();
        triviaQuestion1.setQuestion("What is the largest Country in Africa by inhabitants?");
        triviaQuestion1.setAnswers(List.of("South Africa", "Congo", "Kenia", "Nigeria"));
        triviaQuestion1.setCorrectAnswer("Nigeria");
        Question triviaQuestion2 = new Question();
        triviaQuestion2.setQuestion("What is the largest Country in Europe by size?");
        triviaQuestion2.setAnswers(List.of("Germany", "Ukraine", "Spain", "Finland"));
        triviaQuestion2.setCorrectAnswer("Ukraine");
        this.questions = List.of(triviaQuestion1, triviaQuestion2);

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

        this.members = new HashMap<Long, User>();
        members.put(testUser1.getId(), testUser1);
        members.put(testUser2.getId(), testUser2);
        members.put(testUser3.getId(), testUser3);

        this.gameRoom = new GameRoom();
        gameRoom.setMembers(members);
        gameRoom.setRoomCode("1111");
        gameRoom.setQuestions(this.questions);
        gameRoom.getVoteController();

        this.game = new Game(this.gameRoom);
    }


    @Test
    public void testImportRoomCode() {

        String gameRoomRoomCode = gameRoom.getRoomCode();
        String gameRoomCode = game.getRoomCode();

        assertEquals(gameRoomRoomCode,gameRoomCode);

    }

    @Test
    public void testImportQuestions() {

        List<Question> gameRoomQuestions = gameRoom.getQuestions();
        List<Question> gameQuestions  = game.getQuestions();

        assertEquals(gameRoomQuestions,gameQuestions);

    }

    @Test
    public void testImportVoteController() {
        VoteController gameRoomVoteController = gameRoom.getVoteController();
        VoteController gameVoteController  = game.getVoteController();

        assertEquals(gameRoomVoteController,gameVoteController);

    }
}
