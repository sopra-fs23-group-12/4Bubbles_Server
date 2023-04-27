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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.security.SecureRandom;

import java.util.stream.Stream;

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
        gameRoom.setMembers(List.of(gameRoom.getLeader()));

    }

    public void setLeaderFromRepo(GameRoom gameRoom){
        Long leaderId = gameRoom.getLeaderUserId();
        User leader = userRepository.findById(leaderId).orElseThrow(
                () -> new EntityNotFoundException("User not found with ID: " + leaderId));
        gameRoom.setLeader(leader);
    }

    public GameRoom addPlayerToGameRoom(GameRoom gameRoom, long userId) {
        //list unmodifiable -> every time pass a new one
        User player = retrieveUserFromRepo(userId);
        List<User> members = Stream.concat(gameRoom.getMembers().stream(), Stream.of(player)).toList();
        gameRoom.setMembers(members);
        return gameRoom;
    }

    public void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }
}
