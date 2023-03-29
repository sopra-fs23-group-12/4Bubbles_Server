package ch.uzh.ifi.hase.soprafs23.service;
import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class GameRoomService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public GameRoomService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GameRoom createGameRoom(GameRoom gameRoom) {
        gameRoom.generateRoomCode();
        gameRoom.setLeader(null);

        return gameRoom;
        
    }
    

    
}
