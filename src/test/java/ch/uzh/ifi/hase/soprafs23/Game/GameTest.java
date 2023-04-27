package ch.uzh.ifi.hase.soprafs23.Game;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.VoteController;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GameTest {

    private GameRoom gameRoom;
    private List<Question> questions;
    private Game game;
    


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
        gameRoom.setQuestions(this.questions);

        game = new Game(gameRoom/* , mockServer */);
        //game.setVoteController(new VoteController());
    }

    @Test
    public void testRoundCounterDecreases() {
        //SocketIOServer mockServer = mock(SocketIOServer.class, Answers.RETURNS_DEFAULTS);
       

        assertEquals(2,game.getRoundCounter());
        game.startGame();
        assertEquals(0,game.getRoundCounter());
    }

    @Test
    public void testTimerStartedAndReset() {
        /* SocketIOServer mockServer = mock(SocketIOServer.class, Answers.RETURNS_DEFAULTS);
        Game game = new Game(gameRoom, mockServer); */
        assertEquals(10,game.getRemainingTime());

        game.playRound();

        assertEquals(10,game.getRemainingTime());
    }

    @Test
    public void testTimerRunning() {
        /* SocketIOServer mockServer = mock(SocketIOServer.class, Answers.RETURNS_DEFAULTS);
        Game game = new Game(gameRoom, mockServer); */
        assertEquals(10,game.getRemainingTime());

        game.playRound();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000); // wait for 2 seconds
                // call the function here
                assertTrue(9 > game.getRoundCounter());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
