package ch.uzh.ifi.hase.soprafs23.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Service;
import org.unbescape.html.HtmlEscape;

import ch.uzh.ifi.hase.soprafs23.constant.ApiUrls;
import ch.uzh.ifi.hase.soprafs23.exceptions.ApiConnectionError;
import ch.uzh.ifi.hase.soprafs23.game.stateStorage.Question;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TopicGetDTO;


@Service
@Transactional
public class ApiService implements TriviaCaller{

    private List<TopicGetDTO> topicList;

    
    public ApiService() throws ApiConnectionError {
        try{
            this.topicList = getTopicsFromApi();
        }catch(IOException e){
            throw new ApiConnectionError("Something went wrong while accessing the API", e);
        }
        
    }


    public List<TopicGetDTO> getTopicList() {
        return topicList;
    }
    

    public List<Question> getQuestionsFromApi(String url) throws IOException {
        JSONObject questions = getJSON(url);
        List<Question> questionList = new ArrayList<>();
        for (Object q : questions.getJSONArray("results")) {
            Question question = new Question();
            question.setQuestionString(HtmlEscape.unescapeHtml(((JSONObject) q).getString("question")));
            String correctAnswer = (HtmlEscape.unescapeHtml(((JSONObject) q).getString("correct_answer")));
            question.setCorrectAnswer(correctAnswer);
            List<String> answers = turnToStrings(((JSONObject) q).getJSONArray("incorrect_answers"));
            answers.add(correctAnswer);
            Collections.shuffle(answers);
            question.setAnswers(answers);
            questionList.add(question);
        }

        return questionList;
    }

    //returns a list of topics from the api
    private List<TopicGetDTO> getTopicsFromApi() throws IOException {
        JSONObject topics = getJSON(ApiUrls.CATEGORIES.url);
        List<TopicGetDTO> topicsList = new ArrayList<>();
        for (Object cat : topics.getJSONArray("trivia_categories")) {
            TopicGetDTO topic = new TopicGetDTO();
            topic.setId(((JSONObject) cat).getInt("id"));
            topic.setTopicName(((JSONObject) cat).getString("name"));
            topicsList.add(topic);
        }
        return topicsList;
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

    private List<String> turnToStrings(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (Object o : jsonArray) {
            list.add(HtmlEscape.unescapeHtml(o.toString()));
        }
        return list;
    }
    
}
