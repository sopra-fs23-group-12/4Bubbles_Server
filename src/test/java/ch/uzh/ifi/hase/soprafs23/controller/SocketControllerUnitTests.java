package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.ServerCommandLineRunner;
import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.service.SocketService;
import com.corundumstudio.socketio.SocketIOServer;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SocketControllerUnitTests {
    /*@Autowired
    ServerCommandLineRunner serverCommandLineRunner;

    @Mock
    private SocketIOServer server;

    @Mock
    private SocketService socketService;

    @InjectMocks
    private SocketController socketController;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
        serverCommandLineRunner.stopSocketServer();
    }

    @AfterAll
    public void clean(){
        serverCommandLineRunner.stopSocketServer();
    }

    @Test
    public void testSettingUp(){
        MockitoAnnotations.openMocks(this);
        verify(server, times(1)).addEventListener(eq(EventNames.SEND_MESSAGE.eventName),any(),any());
        verify(server, times(1)).addEventListener(eq(EventNames.START_GAME.eventName),any(),any());
        verify(server, times(1)).addEventListener(eq(EventNames.START_TIMER.eventName),any(),any());
        verify(server, times(1)).addEventListener(eq(EventNames.JOIN_ROOM.eventName),any(),any());
        verify(server, times(1)).addEventListener(eq(EventNames.SEND_VOTE.eventName),any(),any());
        verify(server, times(1)).addEventListener(eq(EventNames.REQUEST_RANKING.eventName),any(),any());

    }

}
