package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;

public class UserPostDTO {

  private String username;
  private String password;
  private Date creationDate;
  private String token;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreatedAt(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
