package ch.uzh.ifi.hase.soprafs23.entity;

/* 
 * This class represents the Topic entity
 * It is only used for the conversion of the API call object to the GetTopicDTO
 */

public class Topic {

    private long id;
    private String topicName;


    public String getTopicName() {
        return topicName;
    }
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    
        
}
