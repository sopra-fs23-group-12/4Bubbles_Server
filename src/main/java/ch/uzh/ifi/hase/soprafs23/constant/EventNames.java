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
    SOMEBODY_VOTED("somebody_voted"),
    GET_RANKING("get_ranking"),
    GET_RIGHT_ANSWER("get_right_answer"),
    END_OF_QUESTION("end_of_question"),


    
    //event listeners
    SEND_VOTE("send_vote"),
    START_GAME("start_game"),
    START_TIMER("start_timer"),
    JOIN_ROOM("join_room"),
    SEND_MESSAGE("send_message"),
    USER_LEFT_GAMEROOM("user_left_gameroom"),
    REQUEST_RANKING("request_ranking");



    public String eventName;

    EventNames(String eventName) {
        this.eventName = eventName;
    }

    
    

    
}
