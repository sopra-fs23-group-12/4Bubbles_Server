package ch.uzh.ifi.hase.soprafs23.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;


@Service
@Transactional
public class ApiService {

    @Autowired
    public ApiService() {
    }

    //returns a list of topics from the api
    public List<TopicGetDTO> getTopicsFromApi(String apiURL) throws IOException {
        JSONObject topics = getTopics(apiURL);
        List<TopicGetDTO> topicList = new ArrayList<TopicGetDTO>();
        for (Object cat : topics.getJSONArray("trivia_categories")) {
            TopicGetDTO topic = new TopicGetDTO();
            topic.setId(((JSONObject) cat).getInt("id"));
            topic.setTopicName(((JSONObject) cat).getString("name"));
            topicList.add(topic);
        }
        return topicList;
    }


    //helper method that call the api and returns a JSONObject
    private JSONObject getTopics(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        StringBuilder json = new StringBuilder();
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        try{
            int cp;
            while ((cp = br.read()) != -1) {
                json.append((char) cp);
            }
        } finally {
            br.close();
        }
        
        return new JSONObject(json.toString());
        
    }
    
}
