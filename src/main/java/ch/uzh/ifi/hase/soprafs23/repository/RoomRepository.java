
package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roomRepository")
public interface RoomRepository extends JpaRepository<GameRoom, Long> {

    GameRoom findByCode(long code); //room code has to be unique otherwise fails -> check when added to repo see

    GameRoom findById(long id);
}
