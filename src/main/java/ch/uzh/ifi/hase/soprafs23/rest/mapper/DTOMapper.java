package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);
    

    @Mapping(source = "leaderId", target = "leaderUserId")
    @Mapping(source = "difficulty", target = "difficulty")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "questionTopicId", target = "questionTopicId")
    GameRoom convertGameRoomPostDTOtoEntity(GameRoomPostDTO gameRoomPostDTO);



    GameRoomGetDTO convertEntityToGameRoomGetDTO(GameRoom gameRoom);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthday", target = "birthday")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "creationDate", target = "creationDate")
    User convertUserPutDTOtoEntity(UserPutDTO UserPutDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "id", target = "id")
    LoginGetDTO convertEntityToLoginPostGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "totalPoints", target = "totalPoints")
    @Mapping(source = "totalGamesPlayed", target = "totalGamesPlayed")
    UserStatisticsGetDTO convertEntityToUserStatisticsGetDTO(User user);
}
