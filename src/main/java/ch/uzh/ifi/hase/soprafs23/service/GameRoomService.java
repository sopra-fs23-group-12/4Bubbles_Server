package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.Objects;
import java.security.SecureRandom;


@Service
@Transactional
public class GameRoomService {

    private final UserRepository userRepository;
    private SecureRandom random = new SecureRandom();

    @Autowired
    public GameRoomService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User retrieveUserFromRepo(long userId){
        return userRepository.findById(userId).orElseThrow();
    }

    public void initGameRoom(GameRoom gameRoom) {

        gameRoom.setRoomCode(Integer.toString(random.nextInt(100000, 1000000)));
        //list unmodifiable -> every time pass a new one
        //gameRoom.getMembers().put(gameRoom.getLeader().getId(), gameRoom.getLeader());

    }

    public void setLeaderFromRepo(GameRoom gameRoom){
        Long leaderId = gameRoom.getLeaderUserId();
        User leader = userRepository.findByid(leaderId);
        gameRoom.setLeader(leader);
        
    }

    public GameRoom addPlayerToGameRoom(GameRoom gameRoom, long userId) {
        User player = retrieveUserFromRepo(userId);
        gameRoom.getMembers().put(userId, player);
        return gameRoom;
    }

    public void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }


}
