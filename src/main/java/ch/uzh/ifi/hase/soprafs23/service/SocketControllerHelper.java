package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.EventNames;
import ch.uzh.ifi.hase.soprafs23.controller.SocketController;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.exceptions.GameIsRunningExeption;
import ch.uzh.ifi.hase.soprafs23.exceptions.RoomNotFoundException;
import ch.uzh.ifi.hase.soprafs23.game.Game;
import ch.uzh.ifi.hase.soprafs23.game.GameRanking;
import ch.uzh.ifi.hase.soprafs23.game.VoteController;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.TimerController;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import com.corundumstudio.socketio.SocketIOClient;
import javassist.NotFoundException;
import org.json.JSONObject;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class SocketControllerHelper {

    private final RoomCoordinator roomCoordinator;
    private final SocketService socketService;
    Logger logger = Logger.getLogger(
            SocketController.class.getName());

    private SocketBasics socketBasics;

    public SocketControllerHelper(SocketService socketService) {
        this.socketService = socketService;
        this.roomCoordinator = RoomCoordinator.getInstance();
        this.socketBasics = new SocketBasics();
    }

    public void updateVoteMethod(Long userId, String message, int remainingTime,String roomCode) throws NotFoundException {
        if (remainingTime > 0) {
            GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
            VoteController voteController = gameRoom.getVoteController();
            Game game = gameRoom.getCurrentGame();
            game.setVoteGame(userId, message, remainingTime);
            HashMap<String, Integer> votesHash = socketService.votesListAsMap(voteController.getVotes());
            System.out.println(votesHash);
            socketBasics.sendObjectToRoom(roomCode, EventNames.SOMEBODY_VOTED.eventName, votesHash);
        }
    }

    public void requestRankingMethod(String roomCode){

        // change this round to currentRoundCounter in game
        try{
            GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
            VoteController voteController = gameRoom.getVoteController();
            Map<Long, Vote> votes = voteController.getVotes();
            Game game = gameRoom.getCurrentGame();
            game.decreaseCounter();
            int round = game.getRoundCounter();
            GameRanking gameRanking = game.getRanking();

            // send ranking as a json
            Map<Long, Integer> currentRanking = gameRanking.updateRanking(gameRoom.getQuestions().get(round), votes);
            voteController.resetVotes();

            JSONObject json = new JSONObject(currentRanking);
            JSONObject response = new JSONObject();
            response.append("ranking", json);

            boolean finalRound = false;
            if (gameRoom.getCurrentGame().getRoundCounter() == 0) {
                finalRound = true;
            }
            response.append("final_round", finalRound);
            // append with append method the boolean on whether it is final
            System.out.println(response);
            socketBasics.sendObjectToRoom(roomCode, EventNames.GET_RANKING.eventName, response.toString());

            if (finalRound){
                roomCoordinator.deleteRoom(roomCode);
                return;
            }
            
            // start game after 5 seconds of ranking (get_question will then automatically
            // let the client know the game continues)
            TimerController timerController = new TimerController();
            timerController.setTimer(5);
            timerController.startTimer(roomCode);
            gameRoom.getCurrentGame().startGame();
        } catch (RoomNotFoundException e){
           logger.info("Room not found");
        }
    }

    public void startTimerMethod(String roomCode) {
        logger.info("timer has been started:");
        logger.info(roomCode);
    }

    public void socketStartGameMethod(String roomCode) throws NotFoundException {
        GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
        logger.info("This game was started:");
        logger.info(String.valueOf(roomCode));
        Game game = new Game(gameRoom);
        gameRoom.setCurrentGame(game);
        gameRoom.setGameStarted(true);
        game.startPreGame();
        game.startGame();
    }

    public void onChatReceivedMethod(SocketIOClient senderClient, String message, String roomCode, String userId) {
        System.out.println("message received:");
        logger.info(senderClient.getHandshakeData().getHttpHeaders().toString());
        logger.info(message);
        logger.info(roomCode);
        logger.info(userId);
        socketBasics.sendObject(EventNames.GET_MESSAGE.eventName, "hello this is the server", senderClient);
    }

    public void joinRoomMethod(SocketIOClient senderClient, String roomCode, Long userId, String bearerToken) {

        // if a room is specified (and exists) and passed with the url, you sign the
        // user into the room. Otherwise, a room is created that has the name of the
        // socket id
        try {
            // join the gameRoom (server entitiy)
            socketService.joinRoom(roomCode, userId, bearerToken, senderClient);
            // join the socket namespace
            senderClient.joinRoom(roomCode);
            logger.info("room is joined!");
            logger.info(roomCode);

            // prepare the response:
            GameRoom gameRoom = roomCoordinator.getRoomByCode(roomCode);
            logger.info("sending room data");
            // sends the room info to the newly joined client
            socketBasics.sendObjectToRoom(roomCode, EventNames.ROOM_IS_JOINED.eventName,
                    DTOMapper.INSTANCE.convertEntityToGameRoomGetDTO(gameRoom));

            // notifies all clients that are already joined that there is a new member
            socketService.sendMemberArray(roomCode, senderClient);
        } catch (Exception e) {
            logger.info("room could not be joined, either room was null or no room with that code exists");
            logger.info(e.toString());
        }

        logger.info("Socket ID[{}]  Connected to socket");
        logger.info(senderClient.getSessionId().toString());
    }

    public void leaveRoomMethod(SocketIOClient senderClient, String roomCode, Long userId) throws NotFoundException {
        GameRoom room = roomCoordinator.getRoomByCode(roomCode);

        try {
            // leave the gameRoom (server entity)
            socketService.removePlayerFromGameRoom(room, userId);
            // leave the socket namespace
            senderClient.leaveRoom(roomCode);
            logger.info("room was left!");
            logger.info(roomCode);

            // notifies all clients that are already joined that there is a new member
            socketService.sendMemberArray(roomCode, senderClient);
        } catch (Exception e) {
            logger.info("room could not be left, either room was null or no room with that code exists");
            logger.info(e.toString());

        }
        logger.info("Socket ID[{}]  Connected to socket");
        logger.info(senderClient.getSessionId().toString());
    }

    public void onConnectedMethod(SocketIOClient senderClient) {
        String roomCode = senderClient.getHandshakeData().getSingleUrlParam("roomCode");
        if (roomCode != null) {
            senderClient.joinRoom(roomCode);
        }
        roomCode = (senderClient.getSessionId().toString());
        senderClient.joinRoom(roomCode);
        logger.info("session id was made into a room");
        logger.info("Socket ID[{}]  Connected to socket");
        logger.info(roomCode);
        socketService.sendObject(senderClient, EventNames.GET_MESSAGE.eventName,
                String.format("single namespace: joined room: %s", roomCode));
    }

    public void onDisconectedMethod(SocketIOClient client) {
        logger.info("Client[{}] - Disconnected from socket");
        logger.info(client.getSessionId().toString());
        System.out.print("\n\n DISCONNECT!! \n\n");
    }
}
