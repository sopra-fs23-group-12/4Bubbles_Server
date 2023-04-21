package ch.uzh.ifi.hase.soprafs23.constant;

public enum EventNames {
    //send events
    GET_ANSWERS("get_answers"),
    GET_QUESTION("get_question"),
    GET_RANKING("get_ranking"),
    GET_MESSAGE("get_message"),
    JOINED_PLAYERS("joined_players"),
    ROOM_IS_JOINED("room_is_joined"),

    
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
