package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AuthenticationService{

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticateUser(User externalUser) {
        User user = this.userRepository.findByUsername(externalUser.getUsername());
        if (externalUser.getPassword().equals("") || externalUser.getUsername().equals("")) {
            String baseErrorMessage = "Oups, your request is wrong. Please full out all fields!";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }
        if (externalUser.getPassword().length() > 20 || externalUser.getUsername().length() > 20) {
            String baseErrorMessage = "Please don't use more than 20 characters for username or password!";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }
        if (user == null || !externalUser.getPassword().equals(user.getPassword())) {
            log.debug("Created Information for User: {}", user);
            String baseErrorMessage = "Wrong credentials!";
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format(baseErrorMessage, "requested user"));
        }
        log.debug("Created Information for User: {}", user);
        user.setStatus(UserStatus.ONLINE);
        return user;
    }

    //changed to void as there is no return value needed
    public void logout(String token) {
        User user = this.userRepository.findByToken(token);
        user.setStatus(UserStatus.OFFLINE);
        //return user;
    }

}
