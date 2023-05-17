package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class ApiServiceTest {
    
    private ApiService apiService = new ApiService();

    @Test
    public void test_getTopicsFromApi_successful() {
        assertFalse(apiService.getTopicList().isEmpty());
    }

    /* @Test
    public void test_getTopicsFromApi_throwsExeption(){

        assertThrows( IOException.class, () -> apiService.getTopicList(""));

    } */
}
