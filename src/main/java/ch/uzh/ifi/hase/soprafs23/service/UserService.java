package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPointsPutDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(String bearerToken) {
        authenticateUser(bearerToken);

        return this.userRepository.findAll();
    }

    public User getUser(Long id, String bearerToken) {
        authenticateUser(bearerToken);
        checkIfExists(id);
        return this.userRepository.findById(id).get();
    }

    public User updateUser(Long id, String bearerToken, User newUser) {

        authenticateUser(bearerToken);
        checkIfExists(id);

        // if no body is set throw bad request
        if (newUser.getUsername() == null && newUser.getBirthday() == null) {
            String baseErrorMessage = "Oups, your request is wrong. Either username or birthday should be set!";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }

        // check if new username is not empty
        if (newUser.getUsername().equals("")) {
            String baseErrorMessage = "Oups, your request is wrong. The username cannot be empty! ";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }
        
        User user = this.userRepository.findById(id).get();

        if (!newUser.getUsername().equals(user.getUsername()) && newUser.getUsername() != null) {
            checkIfUsernameIsUnique(newUser);
            user.setUsername(newUser.getUsername());
        }

        if (newUser.getBirthday() != null) {
            user.setBirthday(newUser.getBirthday());
        }

        user = userRepository.save(user);
        userRepository.flush();

        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUserStats(UserPointsPutDTO userPointsPutDTO) {
        long id = userPointsPutDTO.getId();
        int points = userPointsPutDTO.getPoints();
        checkIfExists(id);
        User user = this.userRepository.findById(id).get();
        user.increaseTotalPoints(points); 
        user.increaseTotalGamesPlayed();
        user = userRepository.save(user);
        userRepository.flush();
    }

    public User createUser(User newUser) {

        checkIfUsernameIsUnique(newUser);

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setCreationDate(new Date());

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User registerUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(new Date());
        checkIfUsernameIsUnique(newUser);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUsernameIsUnique(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "User with username %s already exists, please choose another username.";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage, userToBeCreated.getUsername()));
        }
    }

    private void checkIfExists(Long requestedId) {
        Optional<User> userById = userRepository.findById(requestedId);
        if (!userById.isPresent()) {
            String baseErrorMessage = "The user with id %s was not found.";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, requestedId));
        }
    }

    private void authenticateUser(String bearerToken) {

        String token = bearerToken.replace("Bearer ", "");

        if (userRepository.findByToken(token) == null) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }
}
