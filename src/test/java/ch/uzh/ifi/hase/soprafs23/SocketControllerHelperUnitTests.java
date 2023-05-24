package ch.uzh.ifi.hase.soprafs23;

import ch.uzh.ifi.hase.soprafs23.ServerCommandLineRunner;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.entity.VoteMessage;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.service.SocketControllerHelper;
import ch.uzh.ifi.hase.soprafs23.service.SocketService;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.protocol.Packet;
import javassist.NotFoundException;
import org.aspectj.apache.bcel.classfile.Module;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.http.HttpHeaders;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SocketControllerHelperUnitTests {

    @Autowired
    ServerCommandLineRunner serverCommandLineRunner;

    @Mock
    private SocketIOClient senderClient;
    @Mock
    private SocketIONamespace socketIONamespace;

    @Mock
    private SocketService socketService;

    @Autowired
    private SocketControllerHelper socketControllerHelper;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomCode("333444");

        Question question = new Question();
        question.setQuestion("Largest city in the world?");
        question.setAnswers(List.of("Los Angeles", " Delhi", "Tokyo", "London"));
        gameRoom.setQuestions(List.of(question));

        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);

        roomCoordinator.addRoom(gameRoom);
    }

    @AfterEach
    public void cleanRoomCoordinator(){
        RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();
        roomCoordinator.deleteRoom("333444");
    }

    @Test
    public void testSendRightAnswerMethod() throws NotFoundException {
        assertDoesNotThrow(() -> socketControllerHelper.sendRightAnswerMethod("333444"));
    }

    @Test
    public void testUpdateVoteMethod() throws NotFoundException {
        assertDoesNotThrow(() -> socketControllerHelper.updateVoteMethod(1L, "Tokyo", 5, "333444"));

    }

    @Test
    public void testRequestRankingMethod() throws NotFoundException {
        assertDoesNotThrow(() ->socketControllerHelper.requestRankingMethod("333444"));
    }

    @Test
    public void testStartTimerMethod() throws NotFoundException {
        assertDoesNotThrow(() -> socketControllerHelper.startTimerMethod("333444"));
    }

    @Test
    public void testStartGameMethod() throws NotFoundException {
        assertDoesNotThrow(() ->socketControllerHelper.socketStartGameMethod("333444"));
    }

    @Test
    public void testOnChatReceivedMethod(){
        io.netty.handler.codec.http.HttpHeaders httpHeaders = Mockito.mock(io.netty.handler.codec.http.HttpHeaders.class);
        InetSocketAddress inetSocketAddress = Mockito.mock(InetSocketAddress.class);

        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());
        when(senderClient.getHandshakeData()).thenReturn(new HandshakeData(httpHeaders, Map.of(),inetSocketAddress, "url", false));

        assertDoesNotThrow(() ->socketControllerHelper.onChatReceivedMethod(senderClient, "Hello", "333444", "1"));

        verify(senderClient, times(1)).getHandshakeData();
    }

    @Test
    public void testJoinRoomMethod(){
        //SocketService spySocketService = Mockito.spy(socketservice);

        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());
        //doNothing().when(spySocketservice).joinRoom(any(), any(), any(), any());

        assertDoesNotThrow(() -> socketControllerHelper.joinRoomMethod(senderClient, "333444", 1L, "bearerToken"));

        verify(senderClient, times(1)).getSessionId();
    }

    @Test
    public void testLeaveRoomMethod() throws NotFoundException {
        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> socketControllerHelper.leaveRoomMethod(senderClient, "333444", 1L));

        verify(senderClient, times(1)).getSessionId();
    }

    @Test
    public void testOnConnectMethod(){
        io.netty.handler.codec.http.HttpHeaders httpHeaders = Mockito.mock(io.netty.handler.codec.http.HttpHeaders.class);
        InetSocketAddress inetSocketAddress = Mockito.mock(InetSocketAddress.class);

        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());
        when(senderClient.getHandshakeData()).thenReturn(new HandshakeData(httpHeaders, Map.of(),inetSocketAddress, "url", false));

        assertDoesNotThrow(() -> socketControllerHelper.onConnectedMethod(senderClient));

        verify(senderClient, times(1)).getSessionId();

    }

    @Test
    public void testOnDisconnectMethod(){
        when(senderClient.getNamespace()).thenReturn(socketIONamespace);
        when(senderClient.getSessionId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> socketControllerHelper.onDisconectedMethod(senderClient));

        verify(senderClient, times(1)).getSessionId();
    }
}
