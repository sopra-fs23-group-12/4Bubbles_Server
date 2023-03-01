package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authorization) {

    // check if authorization is set
    if (Objects.isNull(authorization)) {
      String baseErrorMessage = "You need to log in to see this information.";
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
    }

    String token = authorization.replace("Bearer ", "");

    // fetch all users in the internal representation
    List<User> users = userService.getUsers(token);
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @GetMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getUser(@PathVariable Long id,
      @RequestHeader(value = "Authorization", required = false) String authorization) {

    // check if authorization is set
    if (Objects.isNull(authorization)) {
      String baseErrorMessage = "You need to log in to see this information.";
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
    }

    // Bearer Token --> check if User is allowed to get this Resource
    String token = authorization.replace("Bearer ", "");

    Optional<User> optionalUser = userService.getUser(id, token);
    User user = optionalUser.get();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));

    return userGetDTOs;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @PutMapping("/users/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateUser(@PathVariable Long id, @RequestBody UserPutDTO userPutDTO) {

    User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

    User createdUser = userService.updateUser(id, userInput);

  }

}
