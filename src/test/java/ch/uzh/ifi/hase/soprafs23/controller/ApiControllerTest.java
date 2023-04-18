package ch.uzh.ifi.hase.soprafs23.controller;


import java.util.Collections;
import java.util.List;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;


import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;
import ch.uzh.ifi.hase.soprafs23.service.ApiService;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiService apiService;
    
    /* @Test
    public void getTopicsTest_successful() throws Exception {
        //given
        TopicGetDTO topic = new TopicGetDTO();
        String testUrl = "https://opentdb.com/api_category.php";
        topic.setId(1);
        topic.setTopicName("TestTopic");
        List<TopicGetDTO> topics = Collections.singletonList(topic);

        given(apiService.getTopicsFromApi(testUrl)).willReturn(topics);

        //when
        MockHttpServletRequestBuilder request = get("/categories").contentType("application/json")
            .header("Authorization", "Bearer " + "top-secret-token");

        //then
        mockMvc.perform(request).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].topicName", is("TestTopic")));

            
    } */

}
