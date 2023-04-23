package ch.uzh.ifi.hase.soprafs23.constant;

public enum EventNames {
    //send events
    GET_ANSWERS("get_answers"),
    GET_QUESTION("get_question"),
    GET_MESSAGE("get_message"),
    JOINED_PLAYERS("joined_players"),
    ROOM_IS_JOINED("room_is_joined"),
    TIMER_COUNT("timer_count"),
    GAME_STARTED("game_started"),
    RECEIVE_VOTING("receive_voting"),

    
    //event listeners
    SEND_VOTE("send_vote"),
    START_GAME("start_game"),
    START_TIMER("start_timer"),
    JOIN_ROOM("join_room"),
    SEND_MESSAGE("send_message");

    public String eventName;

    EventNames(String eventName) {
        this.eventName = eventName;
    }

    
    

    
}
