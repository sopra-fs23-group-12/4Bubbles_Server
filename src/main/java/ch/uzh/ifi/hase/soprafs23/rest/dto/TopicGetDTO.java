package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class TopicGetDTO {
    
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
