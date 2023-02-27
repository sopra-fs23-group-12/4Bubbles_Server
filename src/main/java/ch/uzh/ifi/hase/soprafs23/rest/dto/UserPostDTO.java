package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.sql.Timestamp;

public class UserPostDTO {


  private String username;
  private String password;
  private Timestamp createdAt;


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

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }
}
