package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper(componentModel = "spring")
public abstract  class DTOMapper {

    @Mapping(source = "leader", target = "leader", qualifiedByName = "retrieveLeaderUser")
    @Mapping(source = "gameMode", target = "gameMode", qualifiedByName = "retrieveLeaderUser")
    public abstract GameRoom convertGameRoomPostDTOtoEntity(GameRoomPostDTO gameRoomPostDTO);

    @Autowired
    UserRepository userRepository;

    @Named("retrieveLeaderUser")
    public User retrieveLeaderUser(Long leaderId) {
        return userRepository.findById(leaderId).orElseThrow(
                () -> new EntityNotFoundException("User not found with ID: " + leaderId));
    }

    public abstract GameRoomGetDTO convertEntityToGameRoomGetDTO(GameRoom gameRoom);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    public abstract User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthday", target = "birthday")
    public abstract  UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "creationDate", target = "creationDate")
    public abstract User convertUserPutDTOtoEntity(UserPutDTO UserPutDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "id", target = "id")
    public abstract LoginGetDTO convertEntityToLoginPostGetDTO(User user);
}
