package ch.uzh.ifi.hase.soprafs23.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EventNamesTest {

    @Test
    void testEventName() {
        EventNames GET_ANSWERS = EventNames.GET_ANSWERS;
        assertEquals("get_answers", GET_ANSWERS.eventName);
    }
    
}