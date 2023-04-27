package ch.uzh.ifi.hase.soprafs23.constant;

import org.junit.jupiter.api.Test;

public class EventNametest {

    @Test
    public void testEventName() {
        EventNames GET_ANSWERS = EventNames.GET_ANSWERS;
        assert (GET_ANSWERS.toString().equals("GET_ANSWERS"));
    }
    
}
