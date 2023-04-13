package ch.uzh.ifi.hase.soprafs23.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONObject;

import org.springframework.stereotype.Service;


import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;


@Service
@Transactional
public class ApiService {



    //returns a list of topics from the api
    public List<TopicGetDTO> getTopicsFromApi(String apiURL) throws IOException {
        JSONObject topics = getJSON(apiURL);
        List<TopicGetDTO> topicList = new ArrayList<>();
        for (Object cat : topics.getJSONArray("trivia_categories")) {
            TopicGetDTO topic = new TopicGetDTO();
            topic.setId(((JSONObject) cat).getInt("id"));
            topic.setTopicName(((JSONObject) cat).getString("name"));
            topicList.add(topic);
        }
        return topicList;
    }

    public List<QuestionGetDTO> getQuestionsFromApi(String url) throws IOException {
        JSONObject questions = getJSON(url);
        List<QuestionGetDTO> questionList = new ArrayList<>();
        for (Object q : questions.getJSONArray("results")) {
            QuestionGetDTO question = new QuestionGetDTO();
            question.setQuestion(((JSONObject) q).getString("question"));
            question.setCorrectAnswer(((JSONObject) q).getString("correct_answer"));
            question.setIncorrectAnswers(((JSONObject) q).getJSONArray("incorrect_answers").toList());
            questionList.add(question);
        }

        return questionList;
    }


    //helper method that call the api and returns a JSONObject
    private JSONObject getJSON(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        StringBuilder json = new StringBuilder();
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
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


    /* public static void main(String[] args) throws IOException {
        ApiService apiService = new ApiService();
        System.out.println(apiService.getTQuestionsFromApi(10,9));
    } */
    
}
