package ch.uzh.ifi.hase.soprafs23.constant;

public enum ApiUrls {
    CATEGORIES("https://opentdb.com/api_category.php"),
    //%d is a placeholder for an integer, amout of questions and category id
    //difficulty is set to medium and type to multiple choice by default can be changed later
    QUESTIONS("https://opentdb.com/api.php?amount=%d&category=%d&difficulty=%s&type=multiple"); 

    public String url;

    ApiUrls(String url) {
        this.url = url;
    }
    
}


