package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

public class UserPutDTO {

  private Long id;
  private String username;
  private UserStatus status;
  private Date creationDate;
  private Date birthday;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) throws ParseException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate date = LocalDate.parse(birthday, formatter);
    ZoneId defaultZoneId = ZoneId.of("UTC");
    this.birthday = Date.from(date.atStartOfDay(defaultZoneId).toInstant());

  }
}
