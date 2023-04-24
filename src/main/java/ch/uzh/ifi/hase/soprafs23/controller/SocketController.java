package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.VoteMessage;
import ch.uzh.ifi.hase.soprafs23.game.VoteController;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.service.SocketService;
import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.Message;
import ch.uzh.ifi.hase.soprafs23.entity.RoomCoordinator;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;

import ch.uzh.ifi.hase.soprafs23.service.SocketBasics;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
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
    Logger logger
            = Logger.getLogger(
            SocketController.class.getName());


    private final SocketIOServer server;
    private final SocketService socketService;
    private final SocketBasics socketBasics = new SocketBasics();

    private RoomCoordinator roomCoordinator = RoomCoordinator.getInstance();

    private HashMap<String, VoteController> voteControllerHashMap = new HashMap<>();

    public SocketController(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;

        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(EventNames.SEND_MESSAGE.eventName, Message.class, onChatReceived());
        server.addEventListener(EventNames.START_GAME.eventName, Message.class, socketStartGame());
        server.addEventListener(EventNames.START_TIMER.eventName, Message.class, startTimer());
        server.addEventListener(EventNames.JOIN_ROOM.eventName, Message.class, joinRoom());
        server.addEventListener(EventNames.SEND_VOTE.eventName, VoteMessage.class, updateVote());

    }


    private DataListener<VoteMessage> updateVote(){
        return (senderClient, data, ackSender) -> {
            System.out.println("data listener in socketcontroller");
            VoteController voteController = voteControllerHashMap.get(data.getRoomCode());
            if (data.getRemainingTime() > 0)
                System.out.printf("userid : %s \n", data.getUserId());
                voteController.setVote(Long.valueOf(data.getUserId()), data.getMessage(), data.getRemainingTime());
        };
        }




    //example implementation on /websockets, call this method to start the timer
    private DataListener<Message> startTimer() {
        return (senderClient, data, ackSender) -> {
            logger.info( "timer has been started:");
            logger.info(data.getRoomCode());
            //socketService.timerExample(data.getRoomCode());
         };
    }


    private DataListener<Message> socketStartGame() {
        return (senderClient, data, ackSender) -> {
            GameRoom gameRoom = roomCoordinator.getRoomByCode(data.getRoomCode());
            logger.info( "This game was started:");
            logger.info(String.valueOf(data.getRoomCode()));
            VoteController voteController = new VoteController();
            voteControllerHashMap.put(data.getRoomCode(), voteController);
            //next part is hard coded questions until the question API call is modified such that the questions are stored in gameroom
            ArrayList<Question> questions = new ArrayList<Question>();
            ArrayList<String> answers = new ArrayList<String>();
            answers.add("question1");
            answers.add("question2");
            Question question1 = new Question();
            question1.setQuestion("first sample question");
            question1.setCorrectAnswer("question1");
            question1.setAnswers(answers);
            Question question2 = new Question();
            question2.setQuestion("second sample question");
            question2.setCorrectAnswer("question2");
            question2.setAnswers(answers);
            questions.add(question1);
            questions.add(question2);
            gameRoom.setQuestions(questions);


            Game game = new Game(gameRoom);
            game.setVoteController(voteController);
            game.startGame();
        };
    }

    //great to use for debugging, sends a message to every member of a namespace upon receiving a message
    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            System.out.println("message received:");
            logger.info(senderClient.getHandshakeData().getHttpHeaders().toString());
            logger.info(data.getMessage());
            logger.info(String.valueOf(data.getRoomCode()));
            logger.info(String.valueOf(data.getUserId()));
            socketBasics.sendObject(data.getRoomCode(),EventNames.GET_MESSAGE.eventName, "hello this is the server");

        };
    }


    //this method is only called once the gameroom has been created.
    //join a gameRoom and the socketio namespace with the same code
    private DataListener<Message> joinRoom() {
        return (senderClient, data, ackSender) -> {

            String roomCode = data.getRoomCode();
            Long userId = Long.parseLong(data.getUserId());
            String bearerToken = data.getBearerToken();

            //if a room is specified (and exists) and passed with the url, you sign the user into the room. Otherwise, a room is created that has the name of the socket id
            try {
                //join the gameRoom (server entitiy)
                socketService.joinRoom(roomCode, userId, bearerToken, senderClient);
                //join the socket namespace
                senderClient.joinRoom(roomCode);
                logger.info("room is joined!");
                logger.info(roomCode);

                //prepare the response:
                GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
                logger.info("sending room data");
                //sends the room info to the newly joined client
                socketBasics.sendObject(roomCode, EventNames.ROOM_IS_JOINED.eventName, DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoom));

                //notifies all clients that are already joined that there is a new member
                socketService.sendMemberArray(roomCode,senderClient);
            }
            catch (Exception e) {
                logger.info("room could not be joined, either room was null or no room with that code exists");
                logger.info(e.toString());

            }
            logger.info("Socket ID[{}]  Connected to socket");
            logger.info(senderClient.getSessionId().toString());
        };
    }



    private ConnectListener onConnected() {
        return (senderClient) -> {
            String roomCode = senderClient.getHandshakeData().getSingleUrlParam("roomCode");
            if (roomCode != null){
                senderClient.joinRoom(roomCode);
            }
            roomCode = (senderClient.getSessionId().toString());
            senderClient.joinRoom(roomCode);
            logger.info("session id was made into a room");
            logger.info("Socket ID[{}]  Connected to socket");
            logger.info(roomCode);
            socketService.sendObject(roomCode, EventNames.GET_MESSAGE.eventName, String.format("single namespace: joined room: %s", roomCode));

        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            logger.info("Client[{}] - Disconnected from socket");
            logger.info(client.getSessionId().toString());
            System.out.print("\n\n DISCONNECT!! \n\n");
        };
    }

}

