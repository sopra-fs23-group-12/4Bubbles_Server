package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.game.GameRanking;
import ch.uzh.ifi.hase.soprafs23.game.VoteController;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.service.SocketControllerHelper;
import ch.uzh.ifi.hase.soprafs23.service.SocketService;
import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

import ch.uzh.ifi.hase.soprafs23.service.SocketBasics;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/*
    server.addConnectListener(onConnected()) => triggers when someone connect to the socket
    server.addDisconnectListener(onDisconnected()) => triggers when someone disconnect from the socket
    server.addEventListener(“send_message”, Message.class, onChatReceived()) => triggers if an event with the correct event name happens
    senderClient.getNamespace().getBroadcastOperations().sendEvent(“get_message”, data.getMessage()) => sending data to all of the clients, includes yourself

 How to add a server endpoint:

 1. Know the incoming data & eventName

 2. Register the EventListener in SocketController with the correct eventName, incoming object and the name of the method you want it to trigger

         server.addEventListener(*"Event_Name"*, *receive this object*, *method call*);

 3. Specify the method in the SocketController class, you can extract functionalities and add them to SocketService when useful or use methods already defined in SocketService

        private DataListener<Message> *method name*() {
            return (senderClient, data, ackSender) -> {
                TODO ...
     }


 How to send a message to a / many client(s):

 1. You need to know the SocketIOClient for sending to one client and one member of a namespace for sending a message to a namespace, the roomCode for both, and the eventName

 2. Use the methods specified in SocketService; sendMessage() for sending a string, and sendRoomData() for sending an unspecified object (currently used for sending GameRoomData)
 or you can specify how you want to send things yourself and use the client.sendEvent() method. The client is an objet of the SocketIOClient class. (for example look at the implementation of sendMessage)

 3. Make sure the client listens to the event in a useEffect hook, using socket.on() ...

 */

@Slf4j
@Component
public class SocketController {

    // Create a Logger debugging
    Logger logger = Logger.getLogger(
            SocketController.class.getName());

    private final SocketIOServer server;
    private final SocketService socketService;

    private SocketControllerHelper socketControllerHelper;

    public SocketController(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        this.socketControllerHelper = new SocketControllerHelper(this.socketService);

        addEventListeners(server);
    }

    public void addEventListeners(SocketIOServer server) {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(EventNames.SEND_MESSAGE.eventName, Message.class, onChatReceived());
        server.addEventListener(EventNames.START_GAME.eventName, Message.class, socketStartGame());
        server.addEventListener(EventNames.START_TIMER.eventName, Message.class, startTimer());
        server.addEventListener(EventNames.JOIN_ROOM.eventName, Message.class, joinRoom());
        server.addEventListener(EventNames.SEND_VOTE.eventName, VoteMessage.class, updateVote());
        server.addEventListener(EventNames.REQUEST_RANKING.eventName, Message.class, requestRanking());
        server.addEventListener(EventNames.USER_LEFT_GAMEROOM.eventName, Message.class, leaveRoom());
    }


    private DataListener<VoteMessage> updateVote() {
        return (senderClient, data, ackSender) -> {socketControllerHelper.updateVoteMethod(Long.parseLong(data.getUserId()), data.getMessage(), data.getRemainingTime(), data.getRoomCode());};
    }

    private DataListener<Message> requestRanking() {
        return (senderClient, data, ackSender) -> {socketControllerHelper.requestRankingMethod(data.getRoomCode());};
    }

    // example implementation on /websockets, call this method to start the timer
    private DataListener<Message> startTimer() {
        return (senderClient, data, ackSender) -> {socketControllerHelper.startTimerMethod(data.getRoomCode());};
    }

    private DataListener<Message> socketStartGame() {
        return (senderClient, data, ackSender) -> {socketControllerHelper.socketStartGameMethod(data.getRoomCode());};
    }

    // great to use for debugging, sends a message to every member of a namespace
    // upon receiving a message
    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {socketControllerHelper.onChatReceivedMethod(senderClient, data.getMessage(), data.getRoomCode(), String.valueOf(data.getUserId()));};
    }

    // this method is only called once the gameroom has been created.
    // join a gameRoom and the socketio namespace with the same code
    private DataListener<Message> joinRoom() {
        return (senderClient, data, ackSender) -> {socketControllerHelper.joinRoomMethod(senderClient, data.getRoomCode(), Long.parseLong(data.getUserId()), data.getBearerToken());};
    }

    private DataListener<Message> leaveRoom(){
        return (senderClient, data, ackSender) -> {socketControllerHelper.leaveRoomMethod(senderClient, data.getRoomCode(), Long.parseLong(data.getMessage()));
        };
    }

    private ConnectListener onConnected() {
        return (senderClient) -> {socketControllerHelper.onConnectedMethod(senderClient);};
    }

    private DisconnectListener onDisconnected() {
        return client -> {socketControllerHelper.onDisconectedMethod(client);};
    }
}
