package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;

    public void testRepo(){
        User newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("Fabio");
        newUser.setPassword("pw");
        Date date = new Date(0);
        newUser.setCreationDate(date);
        newUser.setToken("token");
        newUser.setStatus(UserStatus.ONLINE);

        userRepository.save(newUser);
        userRepository.findAll();
    }
}
