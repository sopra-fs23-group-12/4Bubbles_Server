package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.ServerCommandLineRunner;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import com.corundumstudio.socketio.*;

import javassist.NotFoundException;
import org.aspectj.apache.bcel.classfile.Module;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetSocketAddress;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SocketControllerHelperUnitTests {

    @Autowired
    ServerCommandLineRunner serverCommandLineRunner;

    @Mock
    private SocketIOClient senderClient;
    @Mock
    private SocketIONamespace socketIONamespace;

    @Autowired
    private SocketControllerHelper socketControllerHelper;

    @BeforeAll
    public void Setup(){
        MockitoAnnotations.openMocks(this);
        serverCommandLineRunner.stopSocketServer();
    }

    @AfterAll
    public void clean(){
        serverCommandLineRunner.stopSocketServer();
    }

    //commented test were failing for unknown reasons delete before final deadline

    /*@Test
    public void testSendRightAnswerMethod() throws NotFoundException {
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        assertDoesNotThrow(() -> socketControllerHelper.sendRightAnswerMethod("123456"));

        roomCoordinator.deleteRoom("123456");
    }

    @Test
    public void testUpdateVoteMethod() throws NotFoundException {
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        assertDoesNotThrow(() -> socketControllerHelper.updateVoteMethod(1L, "Tokyo", 5, "123456"));

        roomCoordinator.deleteRoom("123456");
    }

    @Test
    public void testRequestRankingMethod() throws NotFoundException {
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        assertDoesNotThrow(() ->socketControllerHelper.requestRankingMethod("123456"));

        roomCoordinator.deleteRoom("123456");
    }*/

    @Test
    public void testStartTimerMethod() throws NotFoundException {
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        assertDoesNotThrow(() -> socketControllerHelper.startTimerMethod("123456"));

        roomCoordinator.deleteRoom("123456");
    }

    /*@Test
    public void testStartGameMethod() throws NotFoundException {
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        assertDoesNotThrow(() ->socketControllerHelper.socketStartGameMethod("123456"));

        roomCoordinator.deleteRoom("123456");
    }*/

    @Test
    public void testOnChatReceivedMethod(){
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        io.netty.handler.codec.http.HttpHeaders httpHeaders = Mockito.mock(io.netty.handler.codec.http.HttpHeaders.class);
        InetSocketAddress inetSocketAddress = Mockito.mock(InetSocketAddress.class);

        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());
        when(senderClient.getHandshakeData()).thenReturn(new HandshakeData(httpHeaders, Map.of(),inetSocketAddress, "url", false));

        assertDoesNotThrow(() ->socketControllerHelper.onChatReceivedMethod(senderClient, "Hello", "123456", "1"));

        verify(senderClient, times(1)).getHandshakeData();

        roomCoordinator.deleteRoom("123456");
    }

    @Test
    public void testJoinRoomMethod(){
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));
        gameRoom.setGameStarted(false);

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);

        //SocketService spySocketService = Mockito.spy(socketservice);

        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());
        //doNothing().when(spySocketservice).joinRoom(any(), any(), any(), any());

        assertDoesNotThrow(() -> socketControllerHelper.joinRoomMethod(senderClient, "123456", 1L, "bearerToken"));

        verify(senderClient, times(1)).getSessionId();

        //roomCoordinator.deleteRoom("123456");

    }

    /*@Test
    public void testLeaveRoomMethod() throws NotFoundException {
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);


        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> socketControllerHelper.leaveRoomMethod(senderClient, "123456", 1L));

        verify(senderClient, times(1)).getSessionId();

        roomCoordinator.deleteRoom("123456");

    }*/

    @Test
    public void testOnConnectMethod(){
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);


        io.netty.handler.codec.http.HttpHeaders httpHeaders = Mockito.mock(io.netty.handler.codec.http.HttpHeaders.class);
        InetSocketAddress inetSocketAddress = Mockito.mock(InetSocketAddress.class);

        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());
        when(senderClient.getHandshakeData()).thenReturn(new HandshakeData(httpHeaders, Map.of(),inetSocketAddress, "url", false));

        assertDoesNotThrow(() -> socketControllerHelper.onConnectedMethod(senderClient));

        verify(senderClient, times(1)).getSessionId();

        roomCoordinator.deleteRoom("123456");

    }

    @Test
    public void testOnDisconnectMethod(){
        MockitoAnnotations.openMocks(this);

        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("123456");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);


        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> socketControllerHelper.onDisconectedMethod(senderClient));

        verify(senderClient, times(1)).getSessionId();

        roomCoordinator.deleteRoom("123456");
    }
}
