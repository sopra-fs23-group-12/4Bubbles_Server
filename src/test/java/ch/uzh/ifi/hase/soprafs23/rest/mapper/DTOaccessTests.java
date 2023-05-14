package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class DTOaccessTests {

    //UserPutDTO
    //TopicGetDTO
    //UserPostDTO
    //GameRoomGetDTO
    //QuestionGetDTO

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
    public void testGameRoomGetDTO() throws Exception {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setLeaderUserId(1);
        gameRoom.setRoomCode("123456");
        gameRoom.setMembers(List.of(testUser1));

        GameRoomGetDTO gameRoomGetDTO = new GameRoomGetDTO();

        gameRoomGetDTO.setMembers(List.of(testUser1, testUser2));

        List<User> members = gameRoomGetDTO.getMembers();

        gameRoomGetDTO.setLeader(testUser1);

        User leader = gameRoom.getLeader();

        gameRoomGetDTO.setNumOfQuestions(5);

        gameRoomGetDTO.getNumOfQuestions();

        gameRoomGetDTO.setQuestionTopic("Geography");

        String topic = gameRoomGetDTO.getQuestionTopic();

        gameRoomGetDTO.setRoomCode("123456");

        String roomCode = gameRoomGetDTO.getRoomCode();

        gameRoomGetDTO.setGameMode("Standard");

        String mode = gameRoomGetDTO.getGameMode();
    }

    @Test
    public void testQuestionGetDTO() throws Exception {

        QuestionGetDTO questionGetDTO = new QuestionGetDTO();

        questionGetDTO.setQuestion("Largest city?");

        String question = questionGetDTO.getQuestion();

        questionGetDTO.setCorrectAnswer("Tokyo");

        questionGetDTO.getCorrectAnswer();

        questionGetDTO.setIncorrectAnswers(List.of("Delhi", "Sao Paolo", "Shanghai"));

        List<Object> incorrectAnswers = questionGetDTO.getIncorrectAnswers();
    }

    @Test
    public void testTopicGetDTO() throws Exception {

        TopicGetDTO topicGetDTO = new TopicGetDTO();

        topicGetDTO.setTopicName("Geography");

        String topicName = topicGetDTO.getTopicName();

        topicGetDTO.setId(1L);

        Long id = topicGetDTO.getId();
    }

    @Test
    public void testUserPostDTO() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();

        userPostDTO.setUsername("Fabio");

        String userName = userPostDTO.getUsername();

        userPostDTO.setPassword("pw");

        String password = userPostDTO.getPassword();

        userPostDTO.setCreatedAt(new Date(0));

        Date creationDate = userPostDTO.getCreationDate();

        userPostDTO.setToken("bearerToken");

        String token = userPostDTO.getToken();
    }

    @Test
    public void testUserPutDTO() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();

        userPutDTO.setId(1L);

        Long id = userPutDTO.getId();

        userPutDTO.setUsername("Fabio");

        String userName = userPutDTO.getUsername();

        userPutDTO.setStatus(UserStatus.OFFLINE);

        UserStatus status = userPutDTO.getStatus();

        userPutDTO.setCreationDate(new Date(0));

        Date creationDate = userPutDTO.getCreationDate();

        userPutDTO.setBirthday("17.02.2003");

        userPutDTO.getBirthday();
    }
}
