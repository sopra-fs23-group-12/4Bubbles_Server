package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class ApiServiceTest {
    
    private ApiService apiService = new ApiService();

    @Test
    public void test_getTopicsFromApi_successful() {

        try {
            assertFalse(apiService.getTopicsFromApi("https://opentdb.com/api_category.php").isEmpty());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void test_getTopicsFromApi_throwsExeption(){

        assertThrows( IOException.class, () -> apiService.getTopicsFromApi(""));

    }
}


